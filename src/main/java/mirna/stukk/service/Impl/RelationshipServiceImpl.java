package mirna.stukk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import mirna.stukk.Pojo.*;
import mirna.stukk.Pojo.DTO.MirnaRelationDTO;
import mirna.stukk.Pojo.DTO.PredictionDTO;
import mirna.stukk.Pojo.search.MirnaRelationSearch;
import mirna.stukk.Pojo.search.MirnaRelationshipData;
import mirna.stukk.config.Result;
import mirna.stukk.mapper.MirnaMapper;
import mirna.stukk.mapper.PredictionMapper;
import mirna.stukk.mapper.RelationshipMapper;
import mirna.stukk.service.DiseaseService;
import mirna.stukk.service.MirnaService;
import mirna.stukk.service.RelationshipService;
import mirna.stukk.utils.BaseException;
import mirna.stukk.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper,RelationShip> implements RelationshipService {


    @Autowired
    private RelationshipMapper relationshipMapper;

    @Autowired
    private MirnaService mirnaService;

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private PredictionMapper predictionMapper;
    @Override
    public Result<Diagram> GetMirnaRelationship(String mirnaName) { //根据mirna名字来找关系图..
        List<Node> nodes = new LinkedList<>();
        List<Link> links = new LinkedList<>();
        List<Category> categories = new LinkedList<>();
        Integer pi = 0;
        int cat = 0;

        Node first = Node.builder().id((pi++).toString()).name(mirnaName).symbolSize(64).category(cat).value(1).build(); //第一个点

        categories.add(new Category("MiRNA:"+mirnaName));
        nodes.add(first); // 将第一个点放入
        MiRNA mirna_name = mirnaService.query().eq("mirna_name", mirnaName).one();
        if(mirna_name == null){
            return Result.success(Diagram.builder().links(links).nodes(nodes).categories(categories).build());
        }
        Long id = mirna_name.getId();
        List<String> diseaseNames = relationshipMapper.getDiseaseListByMirna(mirnaName,null,new Random().nextInt(30));//这些是疾病的名字
        cat++;
        categories.add(new Category("已证实的相关联的疾病")); //已证实的第二层
        for(String p : diseaseNames){
            Node point = Node.builder().id(pi.toString()).name(p).category(cat).symbolSize(44).value(1).build();
            //将点加进去
            nodes.add(point);
            links.add(new Link("0",pi.toString()));
            //之后我们要查找一下每一个diseaseName对应的Mirna 名字
            pi = AddMirna(pi, p, cat+1,nodes,links,id);
        }
        cat ++;
        categories.add(new Category("已证实的相关联的MiRNA")); //加入第三层
        cat++; //预测第二层

        //---------进行预测点的加入----------------
        categories.add(new Category("预测的相关联的疾病")); //加入预测第二层
        List<Prediction> diseaseListPredictionByMirna = predictionMapper.getDiseaseListPredictionByMirna(mirnaName,null,new Random().nextInt(30));
        for(Prediction prediction : diseaseListPredictionByMirna){
            String diseaseName = prediction.getName(); //疾病名字
            Double forecastRelevance = prediction.getForecastRelevance(); //预测数值
            Node node = Node.builder().name(diseaseName).value(forecastRelevance).id(pi.toString()).category(cat).symbolSize(32).build();
            nodes.add(node);
            links.add(new Link("0",pi.toString()));
            pi = AddMirnaPrediction(pi, diseaseName, cat+1,nodes,links,id);
        }
        cat++;
        categories.add(new Category("预测的相关联的MiRNA")); //加入预测第三层
        //---------进行预测点的加入----------------
        return Result.success(Diagram.builder().links(links).nodes(nodes).categories(categories).build());
    }

    private int AddMirnaPrediction(Integer pi, String diseaseName, int cat, List<Node> nodes, List<Link> links, Long id) {
        //第三层的查找、这里要用diseaseName来找mirnaName
        List<Prediction> mirnaListPredictionByDisease = predictionMapper.getMirnaListPredictionByDisease(diseaseName, id, new Random().nextInt(30));
        Integer start = pi;
        for(Prediction prediction : mirnaListPredictionByDisease){
            String mirnaName = prediction.getName();
            Double forecastRelevance =  prediction.getForecastRelevance();
            pi++;
            Node node = new Node();
            node.setValue(forecastRelevance);
            node.setName(mirnaName);
            node.setId(pi.toString());
            node.setCategory(cat);
            node.setSymbolSize(30);
            nodes.add(node);
            links.add(new Link(start.toString(), pi.toString()));
        }
        return pi+1;
    }

    @Override
    public Result<Diagram> GetDiseaseRelationship(String diseaseName) { //疾病对用的关系图
        List<Node> nodes = new LinkedList<>();
        List<Link> links = new LinkedList<>();
        List<Category> categories = new LinkedList<>();
        Integer id = 0; //节点的初始id
        int cat = 0; //节点的类型
        int symbolSize = 64;

        Node firstNode = Node.builder().id((id++).toString()).name(diseaseName).symbolSize(symbolSize).category(cat).value(1).build();

        categories.add(new Category("疾病: "+diseaseName)); //第一层
        nodes.add(firstNode);
        Disease disease = diseaseService.query().eq("disease_name", diseaseName).one();
        if(disease == null){
            return Result.success(Diagram.builder().links(links).nodes(nodes).categories(categories).build());
        }
        Long disease_id = disease.getId();
        cat++;
        categories.add(new Category("已证实的相关联的MiRNA")); //第二层
        List<String> mirnaList = relationshipMapper.getMirnaListByDisease(diseaseName, null,new Random().nextInt(30));
        for(String mirna : mirnaList){
            Node node = new Node();
            node.setName(mirna);
            node.setId(id.toString());
            node.setCategory(cat);
            node.setValue(1);
            node.setSymbolSize(44);
            nodes.add(node);
            Link link = new Link();
            link.setSource("0");
            link.setTarget(id.toString());
            links.add(link);
            id = AddDisease(id, mirna, cat+1 , nodes, links, disease_id,30);
        }
        cat++;
        categories.add(new Category("已证实的相关联的疾病")); //第三层
        cat++; //第四层
        categories.add(new Category("预测的相关联的MiRNA")); //第四层 也就是预测的第二层

        List<Prediction> MirnaList = predictionMapper.getMirnaListPredictionByDisease(diseaseName,null,new Random().nextInt(30));
        for(Prediction prediction : MirnaList){
            String mirnaName = prediction.getName(); //疾病名字
            Double forecastRelevance = prediction.getForecastRelevance(); //预测数值
            Node node = Node.builder().name(mirnaName).value(forecastRelevance).id(id.toString()).category(cat).symbolSize(32).build();
            nodes.add(node);
            links.add(new Link("0",id.toString()));
            id = AddDiseasePrediction(id, mirnaName, cat+1,nodes,links,disease_id);
        }
        cat++;
        categories.add(new Category("预测的相关联的疾病")); //加入预测第三层
        //---------进行预测点的加入----------------
        return Result.success(Diagram.builder().links(links).nodes(nodes).categories(categories).build());
    }

    /*
     * 查询下载页的内容
     * @Param 
     * @return 
     * @author stukk
     * @create 2023-05-29
     **/
    @Override
    public Result<MirnaRelationshipData> getMirnaRelationshipData(MirnaRelationSearch mirnaRelationSearch) {
        //查询Mirna
        List<String> mirnas = mirnaRelationSearch.getMirnas();
        List<String> diseases = mirnaRelationSearch.getDiseases();
        if(mirnas != null && mirnas.size() > 10 || diseases != null &&  diseases.size() > 10){
            throw new BaseException("5001","查询mirna、disease数量过多，请重试");
        }
        Integer pageNum = mirnaRelationSearch.getPageNum();
        Integer pageSize = mirnaRelationSearch.getPageSize();
        Integer resource = mirnaRelationSearch.getResource();
        Double minRelevance = mirnaRelationSearch.getMinRelevance();
        Double maxRelevance = mirnaRelationSearch.getMaxRelevance();
        if(minRelevance > maxRelevance){
            throw new BaseException("5001","查询参数错误,选择的最小关联度大于最大关联度");
        }
        List<MirnaRelationDTO> mirnaRelationDTOList = new LinkedList<>();
        int total = 0;
        int num1 = (pageNum - 1) * pageSize;
        int num2 = pageSize;
        if(resource == 1){
            //已证实
            List<Object> objects = relationshipMapper.getByMessage(mirnas, diseases,num1, num2);
            if(objects == null){
                Result.success();
            }
            total = ((List<Integer>)objects.get(1)).get(0);
            List<RelationShip> relationShipList = ((List<RelationShip>)objects.get(0));
            for(RelationShip relationShip : relationShipList){
                MirnaRelationDTO mirnaRelationDTO = MirnaRelationDTO.builder().pmid(null).mirnaName(relationShip.getMirnaName())
                        .disease(relationShip.getDiseaseName()).relevance(1.0).resource(CommonUtil.HMDD).pmid(relationShip.getPmid()).build();
                mirnaRelationDTOList.add(mirnaRelationDTO);
            }
        }
        else{
//            未证实,预测的
            List<Object> objects = predictionMapper.getPredictionDTO(mirnas, diseases,minRelevance,maxRelevance,num1, num2);
            if(objects == null){
                return Result.success();
            }
            total = ((List<Integer>) objects.get(1)).get(0);
            List<PredictionDTO> predictionDTOList = (List<PredictionDTO>) objects.get(0);
            for(PredictionDTO predictionDTO : predictionDTOList){
                MirnaRelationDTO mirnaRelationDTO = MirnaRelationDTO.builder().pmid(null).mirnaName(predictionDTO.getMirna())
                        .disease(predictionDTO.getDisease()).resource(CommonUtil.PM).relevance(predictionDTO.getForecastRelevance()).build();
                mirnaRelationDTOList.add(mirnaRelationDTO);
            }
        }
        MirnaRelationshipData mirnaRelationshipData = MirnaRelationshipData.builder().total(total).pageNum(pageNum).pageSize(pageSize)
                .mirnaRelationDTOList(mirnaRelationDTOList).build();
        return Result.success(mirnaRelationshipData);
    }

    private int AddDiseasePrediction(Integer id, String mirnaName, int cat, List<Node> nodes, List<Link> links, Long disease_id) {
        //第三层的查找、这里要用mirnaName来找disease
        List<Prediction> diseaseListPredictionByMirna = predictionMapper.getDiseaseListPredictionByMirna(mirnaName, disease_id, new Random().nextInt(30));
        Integer start = id;
        for(Prediction prediction : diseaseListPredictionByMirna){
            String diseaseName = prediction.getName();
            Double forecastRelevance =  prediction.getForecastRelevance();
            id++;
            Node node = new Node();
            node.setValue(forecastRelevance);
            node.setName(diseaseName);
            node.setId(id.toString());
            node.setCategory(cat);
            node.setSymbolSize(30);
            nodes.add(node);
            links.add(new Link(start.toString(), id.toString()));
        }
        return id+1;
    }

    private int AddDisease(Integer id, String mirna, int cat, List<Node> nodes, List<Link> links, Long disease_id,Integer symbolSize) {
        List<String> diseaseList = relationshipMapper.getDiseaseListByMirna(mirna,disease_id,new Random().nextInt(35));
        Integer start = id;
        for(String disease_name: diseaseList){
            id++;
            Node node = new Node();// 第三层的点
            node.setName(disease_name);
            node.setValue(1);
            node.setSymbolSize(symbolSize);
            node.setCategory(cat);
            node.setId(id.toString());
            nodes.add(node);
            links.add(new Link(start.toString(),id.toString()));
        }
        return id+1;
    }

    private int AddMirna(Integer pi, String diseaseName, int cat, List<Node> nodes, List<Link> links,Long id) {
        //我们需要将疾病名字来查询对应的Mirna
        List<String> mirnas = relationshipMapper.getMirnaListByDisease(diseaseName,id,new Random().nextInt(30));
        //相关的mirna
        Integer start = pi;
        for(String p:mirnas){
            pi++;
            Node point = new Node();
            point.setId(pi.toString());
            point.setName(p);
            point.setCategory(cat);
            point.setSymbolSize(30);
            point.setValue(1);
            nodes.add(point);
            links.add(new Link(start.toString(), pi.toString()));//加入一个边
        }
        return pi+1;
    }

}
