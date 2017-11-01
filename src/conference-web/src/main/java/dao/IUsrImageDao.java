package dao;

import beans.UsrImage;

import java.util.Set;

/**
 * Created by NewObject on 2017/8/17.
 */
public interface IUsrImageDao {
    void addImage(UsrImage image);
    Set<UsrImage> queryImageInfoByUserId(Integer uid);
}
