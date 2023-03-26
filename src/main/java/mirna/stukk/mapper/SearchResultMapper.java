package mirna.stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mirna.stukk.Pojo.SearchResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchResultMapper extends BaseMapper<SearchResult> {

    List<Object> getByDisease(Integer pageNum, String diseaseName,String startTime,String endTime,Integer pageSize);



    List<Object> getByMirna(Integer pageNum, String mirnaName,String startTime,String endTime,Integer pageSize);
}
