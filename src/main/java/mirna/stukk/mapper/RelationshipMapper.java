package mirna.stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.Pojo.Relationship_disease;
import mirna.stukk.Pojo.Relationship_mirna;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;

import java.util.List;
import java.util.Map;

@Mapper
public interface RelationshipMapper extends BaseMapper<RelationShip> {
    List<String> getDiseaseListByMirna (String mirnaName,Long id,Integer random); //和这个Mirna有关系的疾病名称

    List<String> getMirnaListByDisease (String diseaseName,Long id,Integer random);

    List<Long> getMirnaIdByDiseaseId(Long disease_id);


    List<RelationShip> getRelationShip();

    int AddRelationshipDisease(List<Relationship_disease> relationshipDiseaseList);

    List<Relationship_disease> getRelationshipDiseaseList();

    List<Relationship_mirna> getRelationshipMirnaList();

    List<Object> getByMessage(List<String> mirnas, List<String> diseases, Integer pageNum, Integer pageSize);

}
