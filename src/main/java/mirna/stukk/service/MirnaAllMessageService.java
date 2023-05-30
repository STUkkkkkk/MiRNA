package mirna.stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import mirna.stukk.Pojo.MirnaAllMessage;
import mirna.stukk.Pojo.Mirna_3P_5P;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-27 19:33
 **/
public interface MirnaAllMessageService extends IService<MirnaAllMessage> {
    Mirna_3P_5P getBy3p5pName(String name);
}
