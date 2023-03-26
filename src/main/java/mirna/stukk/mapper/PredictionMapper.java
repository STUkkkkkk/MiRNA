package mirna.stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mirna.stukk.Pojo.Prediction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PredictionMapper extends BaseMapper<Prediction> {

    List<Prediction> getDiseaseListPredictionByMirna(String mirnaName,Long diseaseId ,Integer random);

    List<Prediction> getMirnaListPredictionByDisease(String diseaseName,Long mirnaId ,Integer random);

}
