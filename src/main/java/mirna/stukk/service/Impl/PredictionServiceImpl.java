package mirna.stukk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mirna.stukk.Pojo.Prediction;
import mirna.stukk.mapper.PredictionMapper;
import mirna.stukk.service.PredictionService;
import org.springframework.stereotype.Service;

@Service
public class PredictionServiceImpl extends ServiceImpl<PredictionMapper, Prediction> implements PredictionService {
}
