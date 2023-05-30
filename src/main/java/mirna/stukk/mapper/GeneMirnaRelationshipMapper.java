package mirna.stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mirna.stukk.Pojo.GeneMirnaRelationship;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-29 22:20
 **/

@Mapper
public interface GeneMirnaRelationshipMapper extends BaseMapper<GeneMirnaRelationship> {
    List<GeneMirnaRelationship> GetByMirnaName(String mirnaName);
}
