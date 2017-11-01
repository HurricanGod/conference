package dao;

import beans.SecurityGuarantee;

import java.util.Map;

/**
 * Created by NewObject on 2017/8/15.
 */


public interface ISecurityGuaranteeDao {
    void addSecurityGuarantee(SecurityGuarantee entity);
    SecurityGuarantee querySecurityGuaranteeByJoin(Map<String, Object> map);
    SecurityGuarantee querySecurityGuaranteeBySingle(Integer id);
}
