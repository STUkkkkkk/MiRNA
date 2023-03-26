package mirna.stukk.service;

import io.swagger.models.auth.In;
import mirna.stukk.config.Result;

public interface SearchResultService {
    Result getByDisease(Integer pageNum, String diseaseName, String startTime, String endTime, Integer pageSize);

    Result getByMirna(Integer pageNum, String mirnaName, String startTime, String endTime, Integer pageSize);
}
