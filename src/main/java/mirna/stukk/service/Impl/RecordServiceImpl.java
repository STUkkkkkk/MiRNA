package mirna.stukk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mirna.stukk.Pojo.Record;
import mirna.stukk.mapper.RecordMapper;
import mirna.stukk.service.RecordService;
import org.springframework.stereotype.Service;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-04-01 0:39
 **/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
}
