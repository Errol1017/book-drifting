package project.basic.controller;

import common.CRUD.service.ComService;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import common.FileProcessor.ZXing.ZXingUtil;
import common.FileProcessor.compress.ZipUtil;
import common.FileProcessor.excel.ExcelUtil;
import common.Util.DateUtil;
import common.Util.InvitationCodeGenerator;
import common.Util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.basic.entity.BookQrCode;
import project.basic.entity.InvitationCode;
import project.navigator.route.Components;
import project.navigator.service.CacheManager;
import project.resource.properties.ServerProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Errol on 17/6/3.
 */
@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

    @ResponseBody
    @RequestMapping(value = Components.Export_Add_Invitation_Code + "/add", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object addInvitationCode(HttpServletRequest request) throws Exception {
        String num = request.getParameter("num");
        List<String> codes = InvitationCodeGenerator.getMany(Integer.parseInt(num));
        List<InvitationCode> list = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();
        List<String> row;
        for (String code : codes) {
            list.add(new InvitationCode(code));
            row = new ArrayList<>();
            row.add(code);
            data.add(row);
        }
        comService.saveDetail(list);
        String filename = ExcelUtil.write2SimpleExcel(data, DateUtil.date2String(new Date(), DateUtil.PATTERN_D) + " 新增邀请码");
        if (filename != null) {
            String key = RandomUtil.getRandomString(11, RandomUtil.PATTEN_ALL_CHARS);
            cacheManager.addElement(key, filename);
            return Result.SUCCESS(key);
        }
        return Result.ERROR(ErrorCode.CODING_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = Components.Export_out_Invitation_Code + "/out", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getInvitationCode(HttpServletRequest request) throws Exception {
        List<InvitationCode> list = comService.getList(InvitationCode.class, "clientId=-1");
        List<List<String>> data = new ArrayList<>();
        List<String> row;
        for (InvitationCode ic : list) {
            row = new ArrayList<>();
            row.add(ic.getCode());
            data.add(row);
        }
        String filename = ExcelUtil.write2SimpleExcel(data, DateUtil.date2String(new Date(), DateUtil.PATTERN_D) + " 之前尚未使用的邀请码");
        if (filename != null) {
            String key = RandomUtil.getRandomString(11, RandomUtil.PATTEN_ALL_CHARS);
            cacheManager.addElement(key, filename);
            return Result.SUCCESS(key);
        }
        return Result.ERROR(ErrorCode.CODING_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = Components.Export_Add_QR_Code + "/add", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object addQRCode(HttpServletRequest request) throws Exception {
        String num = request.getParameter("num");
        List<String> codes = RandomUtil.getRandomString(20, RandomUtil.PATTEN_ALL_CHARS, Integer.parseInt(num));
        List<String> filenames = ZXingUtil.write2File(codes, null);
        List<BookQrCode> list = new ArrayList<>();
        int i = 0;
        for (String code : codes) {
            list.add(new BookQrCode(code, filenames.get(i++)));
        }
        comService.saveDetail(list);
        String compress = ZipUtil.compress(filenames, DateUtil.date2String(new Date(), DateUtil.PATTERN_D) + " 新增图书二维码");
        if (compress != null) {
            String key = RandomUtil.getRandomString(11, RandomUtil.PATTEN_ALL_CHARS);
            cacheManager.addElement(key, compress);
            return Result.SUCCESS(key);
        }
        return Result.ERROR(ErrorCode.CODING_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = Components.Export_out_QR_Code + "/out", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Object getQRCode(HttpServletRequest request) throws Exception {
        List<BookQrCode> list = comService.getList(BookQrCode.class, "bookId=-1");
        List<String> filenames = new ArrayList<>();
        for (BookQrCode bookQrCode: list) {
            filenames.add(bookQrCode.getImgName());
        }
        String compress = ZipUtil.compress(filenames, DateUtil.date2String(new Date(), DateUtil.PATTERN_D) + " 之前尚未使用的图书二维码");
        if (compress != null) {
            String key = RandomUtil.getRandomString(11, RandomUtil.PATTEN_ALL_CHARS);
            cacheManager.addElement(key, compress);
            return Result.SUCCESS(key);
        }
        return Result.ERROR(ErrorCode.CODING_ERROR);
    }


}
