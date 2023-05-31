package mirna.stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import mirna.stukk.Pojo.DTO.MirnaRelationDTO;
import mirna.stukk.Pojo.Diagram;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.Pojo.search.MirnaRelationSearch;
import mirna.stukk.Pojo.search.MirnaRelationshipData;
import mirna.stukk.config.Result;

import java.util.List;

public interface RelationshipService extends IService<RelationShip> {

    Result<Diagram> GetMirnaRelationship(String mirnaName);

    Result<Diagram> GetDiseaseRelationship(String diseaseName);

    Result<MirnaRelationshipData> getMirnaRelationshipData(MirnaRelationSearch mirnaRelationSearch);

    MirnaRelationshipData getMirnaRelationshipDataWay(List<String> mirnas,List<String> diseases , Double minRelevance,Double maxRelevance,int pageNum,int pageSize,int resource );
}
