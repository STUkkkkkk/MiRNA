package mirna.stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mirna.stukk.Pojo.MiRNA;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MirnaMapper extends BaseMapper<MiRNA> {
    List<MiRNA> getAllSearchMiRNA();

    List<MiRNA> getLikeMiRNA(String mirnaName);
}
