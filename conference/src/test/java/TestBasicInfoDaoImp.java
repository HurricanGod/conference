import beans.BasicInfo;
import dao.BasicInfoDaoImp;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by NewObject on 2017/8/13.
 */
public class TestBasicInfoDaoImp {

    private BasicInfoDaoImp daoImp;
    @Before
    public void startTest() {
        daoImp = new BasicInfoDaoImp();
    }

    @Test
    public void addBasicInfoTest() {
        BasicInfo basicInfo = new BasicInfo("刘北台", "男", 23, "17853915041", "中山大学");
        daoImp.addBasicInfo(basicInfo);
    }


    @Test
    public void queryByIdTest() {
        BasicInfo b = daoImp.queryById(1);
        System.out.println(b.toString());
    }

    @Test
    public void queryByIdAndMapperTest() {
        BasicInfo b = daoImp.queryByIdAndMapper(1);
        System.out.println(b.toString());
    }
}
