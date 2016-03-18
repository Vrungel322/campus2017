package com.kpi.campus.ui.presenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.kpi.campus.Config;
import com.kpi.campus.api.service.ServiceCreator;
import com.kpi.campus.api.service.UserService;
import com.kpi.campus.model.User;
import com.kpi.campus.model.pojo.Token;
import com.kpi.campus.ui.Navigator;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * LoginPresenter created to manage LoginActivity.
 *
 * Created by Administrator on 29.01.2016.
 */
public class LoginPresenter extends BasePresenter {

    private IView mView;
    private Navigator mNavigator;
    private boolean mIsUserLogged = false;

    @Inject
    public LoginPresenter(Navigator navigator) {
        mNavigator = navigator;
    }

    public void setView(IView view) {
        mView = view;
    }

    @Override
    public void initializeViewComponent() {
    }

    /**
     * Set necessary View components to "login state"
     * @param login login which is entered by the user
     * @param password password which is entered by the user
     */
    public void login(String login, String password) {
        mView.showLoginProgressDialog();
        mView.showLoginButton(false);
        validateUser(login, password);
    }

    /**
     * Init loader to load data for the user authentication.
     * @param login
     * @param password
     */
    private void validateUser(String login, String password) {
        Bundle args = new Bundle();
        args.putString(Config.KEY_LOGIN, login);
        args.putString(Config.KEY_PASSWORD, password);
        mView.initLoader(args);
    }

    /**
     * Set views to "initial (after login) state".
     * If server returns success, start MainActivity.
     * If not, launch login failed logic.
     * @param token data for the user authentication
     */
    public void setLoaderResult(Token token) {
        mView.dismissProgressDialog();
        mView.showLoginButton(true);
        if(token != null) {
            saveToken(token.getAccessToken());
            loadInfoAboutUser();

            mNavigator.startMainActivity();
        } else {
            mView.onLoginFailed();
        }
    }

    public void saveToken(String token) {
        saveTokenToUserModel(token);
        saveStateToPref(token);
    }

    /**
     * Check if user logged or not.
     * If user if logged, start directly MainActivity.
     * If not, leave LoginActivity (do nothing).
     * @param sharedPrefs reference to SharedPreferences
     */
    public void checkUserIsLogged(SharedPreferences sharedPrefs) {
        mIsUserLogged = sharedPrefs.getBoolean(Config.IS_LOGGED_SHARED_PREF, false);
        if(mIsUserLogged){
            mNavigator.startMainActivity();
        }
    }

    /**
     * Save values to SharedPreferences.
     */
    private void saveStateToPref(String tokenValue) {
        SharedPreferences sharedPrefs = mView.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //Adding values to editor
        editor.putBoolean(Config.IS_LOGGED_SHARED_PREF, true);
        editor.putString(Config.TOKEN_SHARED_PREF, tokenValue);

        //Saving values to editor
        editor.commit();
    }

    private void saveTokenToUserModel(String token) {
        User.getInstance().token = token;
    }

    private void loadInfoAboutUser() {
        UserService service = ServiceCreator.createService(UserService.class);
        Observable<User> observable = service.getUser("bearer " + User.getInstance().token);
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Config.LOG, e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(Config.LOG, "Successful download of information about the User ".concat(User.getInstance().name));
                    }
                });
    }

    public interface IView {
        void showLoginProgressDialog();
        void dismissProgressDialog();
        void showLoginButton(boolean shouldShow);
        void onLoginFailed();
        void initLoader(Bundle args);
        SharedPreferences getSharedPreferences();
    }
}
