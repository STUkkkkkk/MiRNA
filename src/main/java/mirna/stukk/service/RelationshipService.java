package mirna.stukk.service;

import mirna.stukk.Pojo.Diagram;
import mirna.stukk.config.Result;

import java.util.List;

public interface RelationshipService {

    Result<Diagram> GetMirnaRelationship(String mirnaName);

    Result<Diagram> GetDiseaseRelationship(String diseaseName);
}
