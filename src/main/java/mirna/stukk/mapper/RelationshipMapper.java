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
    List<String> getDiseaseListByMirna (String mirnaName,Long id,Integer random);

    List<String> getMirnaListByDisease (String diseaseName,Long id,Integer random);

    List<Long> getMirnaIdByDiseaseId(Long disease_id);


    List<RelationShip> getRelationShip();

    int AddRelationshipDisease(List<Relationship_disease> relationshipDiseaseList);

    List<Relationship_disease> getRelationshipDiseaseList();

    List<Relationship_mirna> getRelationshipMirnaList();


}
