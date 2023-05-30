package mirna.stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mirna.stukk.Pojo.MirnaAllMessage;
import mirna.stukk.Pojo.Mirna_3P_5P;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-27 19:33
 **/
@Mapper
public interface MirnaAllMessageMapper extends BaseMapper<MirnaAllMessage> {
    Mirna_3P_5P getBy3p5pName(String name); //用来找一下3p儿子


}
