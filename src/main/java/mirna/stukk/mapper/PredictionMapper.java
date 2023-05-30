package mirna.stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mirna.stukk.Pojo.DTO.PredictionDTO;
import mirna.stukk.Pojo.Prediction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PredictionMapper extends BaseMapper<Prediction> {

    List<Prediction> getDiseaseListPredictionByMirna(String mirnaName,Long diseaseId ,Integer random);

    List<Prediction> getMirnaListPredictionByDisease(String diseaseName,Long mirnaId ,Integer random);

    List<Object> getPredictionDTO(@Param("mirnas") List<String> mirnas,@Param("diseases") List<String> diseases,@Param("minRelevance") double minRelevance,@Param("maxRelevance") double maxRelevance,int pageNum,int pageSize);

}
