package mirna.stukk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mirna.stukk.Pojo.MirnaAllMessage;
import mirna.stukk.Pojo.Mirna_3P_5P;
import mirna.stukk.mapper.MirnaAllMessageMapper;
import mirna.stukk.service.MirnaAllMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-27 19:35
 **/
@Service
public class MirnaAllMessageServiceImpl extends ServiceImpl<MirnaAllMessageMapper, MirnaAllMessage> implements MirnaAllMessageService {

    @Autowired
    private MirnaAllMessageMapper mirna_AllMessageMapper;


    @Override
    public Mirna_3P_5P getBy3p5pName(String name) {
        return mirna_AllMessageMapper.getBy3p5pName(name);
    }

}
