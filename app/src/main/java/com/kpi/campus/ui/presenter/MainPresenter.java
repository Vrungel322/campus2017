package com.kpi.campus.ui.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.kpi.campus.R;
import com.kpi.campus.model.Subsystem;
import com.kpi.campus.model.pojo.User;
import com.kpi.campus.ui.Navigator;
import com.kpi.campus.ui.Preference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * MainPresenter created to manage MainActivity.
 * <p>
 * Created by Administrator on 01.02.2016.
 */
public class MainPresenter extends BasePresenter {

    private IView mView;
    private Context mContext;
    private Navigator mNavigator;
    private Preference mPreference;
    private boolean mIsUserLogged = false;

    @Inject
    public MainPresenter(Context context, Navigator navigator, Preference
            preference) {
        mContext = context;
        mNavigator = navigator;
        mPreference = preference;
    }

    public void setView(IView view) {
        mView = view;
    }

    @Override
    public void initializeViewComponent() {
        mView.setViewComponent();
    }

    public void startActivityBasedOn(int position) {
        switch (position) {
            case 0:
                mNavigator.startBulletinBoardActivity();
                break;
            case 1:
                break;
        }
    }

    /**
     * User logout
     */
    public void logout() {
        mPreference.saveLogoutInfo();

        User.getInstance().token = "";
        mNavigator.startLoginActivity();
    }

    /**
     * Get list of campus subsystems.
     *
     * @return subsystems
     */
    public List<Subsystem> getData() {
        List<Subsystem> subsystems = new ArrayList<>();
        Resources res = getResources();
        String[] names = getSubsystemNames(res);
        TypedArray icons = getSubsystemIcon(res);
        for (int i = 0; i < names.length && i < icons.length(); i++) {
            Subsystem s = new Subsystem(names[i], icons.getResourceId(i, -1));
            subsystems.add(s);
        }
        return subsystems;
    }

    /**
     * Check if user logged or not.
     * If user if logged and auth token is not expired, start MainActivity.
     * If not, redirect user to LoginActivity.
     */
    public void checkUserIsLogged() {
        Date expDate = mPreference.getTokenExpirationDate();
        Date currentDate = new Date();
        mIsUserLogged = mPreference.getIsLogged();
        if (mIsUserLogged && !currentDate.after(expDate))
            getUserSesionValues();
        else
            mNavigator.startLoginActivity();
    }

    private String[] getSubsystemNames(Resources res) {
        return res.getStringArray(R.array.full_subsystem);
    }

    private TypedArray getSubsystemIcon(Resources res) {
        return res.obtainTypedArray(R.array.full_subsystem_image);
    }

    private Resources getResources() {
        return mContext.getResources();
    }

    private void getUserSesionValues() {
        User user = User.getInstance();
        user.id = mPreference.getUserId();
        user.name = mPreference.getUserName();
        user.position = mPreference.getUserPositions();
        user.subdivision = mPreference.getUserSubdivisions();
        user.isBulletinBoardModerator = mPreference.getIsUserBbModerator();
        user.token = mPreference.getToken();
    }

    public interface IView {
        void setViewComponent();
    }
}
