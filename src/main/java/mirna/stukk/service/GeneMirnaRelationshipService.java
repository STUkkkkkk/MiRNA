package mirna.stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import mirna.stukk.Pojo.GeneMirnaRelationship;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-29 22:23
 **/
public interface GeneMirnaRelationshipService extends IService<GeneMirnaRelationship> {
    List<GeneMirnaRelationship> GetByMirnaName(String mirnaName);
}
