package mirna.stukk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mirna.stukk.Pojo.GeneMirnaRelationship;
import mirna.stukk.mapper.GeneMirnaRelationshipMapper;
import mirna.stukk.service.GeneMirnaRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-29 22:23
 **/
@Service
public class GeneMirnaRelationshipServiceImpl extends ServiceImpl<GeneMirnaRelationshipMapper, GeneMirnaRelationship> implements GeneMirnaRelationshipService {

    @Autowired
    private GeneMirnaRelationshipMapper geneMirnaRelationshipMapper;

    @Override
    public List<GeneMirnaRelationship> GetByMirnaName(String mirnaName) {
        return geneMirnaRelationshipMapper.GetByMirnaName(mirnaName);
    }
}
