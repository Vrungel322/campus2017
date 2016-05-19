package ua.kpi.campus.model.pojo;

import java.util.List;

/**
 * Represents the entity of current logged user.
 * POJO class for convenient GSON serialization.
 * <p>
 * Created by Administrator on 16.03.2016.
 */
public class User {

    private static User mInstance;

    private User() {
    }

    public static User getInstance() {
        if (mInstance == null) {
            mInstance = new User();
        }
        return mInstance;
    }

    public String id;
    public String name;
    public List<Item> position;
    public List<Item> subdivision;
    public List<String> descendantSubdivisions;
    public boolean isBulletinBoardModerator = false;

    public String token;

    public List<Item> getSubdivision() {
        return subdivision;
    }
}
