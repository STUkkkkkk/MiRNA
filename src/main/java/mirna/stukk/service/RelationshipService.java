package mirna.stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import mirna.stukk.Pojo.Diagram;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.config.Result;

import java.util.List;

public interface RelationshipService extends IService<RelationShip> {

    Result<Diagram> GetMirnaRelationship(String mirnaName);

    Result<Diagram> GetDiseaseRelationship(String diseaseName);
}
