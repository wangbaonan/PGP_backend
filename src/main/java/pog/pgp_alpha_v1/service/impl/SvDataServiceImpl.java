package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.SvDataMapper;
import pog.pgp_alpha_v1.model.SampleData;
import pog.pgp_alpha_v1.model.SvData;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.SvDataService;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
* @author 86183
* @description 针对表【sv_data】的数据库操作Service实现
* @createDate 2023-04-25 15:19:06
*/
@Service
public class SvDataServiceImpl extends ServiceImpl<SvDataMapper, SvData>
    implements SvDataService{

    @Resource
    private SampleDataServiceImpl sampleDataService;

    @Override
    public Long saveUploadFile(Long dataId, String filePath, String fileName, Long userId, String md5Hash) {

        // 根据dataId 从数据库中获取对应的文件名前缀并将SV的文件名前缀设置为一致
        SampleData sampleData =  sampleDataService.getOne(new QueryWrapper<SampleData>().eq("dataId",dataId));
        String sampleId = sampleData.getSampleId();
        String snpFilePath = sampleData.getFilePath();
        // 从路径中获取文件名
        File snpFile = new File(snpFilePath);
        String snpFileName = snpFile.getName();

        // 指定两种后缀分别为SV.vcf 和 SV.vcf.gz 然后分别根据不同后缀的情况取出文件名前缀
        // 将SV的文件名前缀设置为一致 由于文件名中不仅包含一个点，所以不能使用lastIndexOf(".")来获取文件名前缀
        String svFileNamePrefix = "";
        String svFilePath = filePath;
        File svFile = new File(svFilePath);
        svFilePath = svFile.getParent();
        String svFileName = svFile.getName();
        String svVcfSuffix = ".SV.vcf";
        String svVcfGzSuffix = ".SV.vcf.gz";
        String snpVcfSuffix = ".vcf";
        String snpVcfGzSuffix = ".vcf.gz";
        if(snpFileName.endsWith(snpVcfSuffix)) {
            svFileNamePrefix = snpFileName.substring(0, snpFileName.length() - snpVcfSuffix.length());
        } else if (snpFileName.endsWith(snpVcfGzSuffix)) {
            svFileNamePrefix = snpFileName.substring(0, snpFileName.length() - snpVcfGzSuffix.length());
        }

        if (filePath.endsWith(svVcfSuffix)){
            // 逻辑错误 应使用SNP的fileName根据snp的后缀来获取sv的文件名前缀
            fileName = svFileNamePrefix + svVcfSuffix;
        }else if (filePath.endsWith(svVcfGzSuffix)){
            fileName = svFileNamePrefix + svVcfGzSuffix;
        }
        Path svNewFilePath = Paths.get(svFilePath,fileName);
        File svNewFile = svNewFilePath.toFile();
        File svOldFile = new File(filePath);
        svOldFile.renameTo(svNewFile); // 根据相同DataId的SNP数据来重命名文件
        String newPath = svNewFile.getPath();

        SvData svData = new SvData();
        svData.setDataId(dataId);
        svData.setFilePath(newPath);
        svData.setSampleId(sampleId);
        svData.setUserId(userId);
        svData.setFileName(fileName);
        svData.setMd5Hash(md5Hash);
        // 如果md5已经存在，也需要保存，和DataId一样，不同用户可能上传相同的文件
        this.save(svData);
        return svData.getDataId();
    }

    @Override
    public SvData getSvData(Long dataId, User user) {
        QueryWrapper<SvData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataId",dataId);
        queryWrapper.eq("userId",user.getId());
        return this.getOne(queryWrapper);
    }

    @Override
    public ArrayList<SvData> getSvDataList(User user) {
        QueryWrapper<SvData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",user.getId());
        return (ArrayList<SvData>) this.list(queryWrapper);
    }


}




