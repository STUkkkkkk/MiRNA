package mirna.stukk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mirna.stukk.Pojo.Disease;
import mirna.stukk.mapper.DiseaseMapper;
import mirna.stukk.service.DiseaseService;
import org.springframework.stereotype.Service;

@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper,Disease> implements DiseaseService {
}
