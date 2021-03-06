package net.csibio.propro.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.csibio.aird.bean.WindowRange;
import net.csibio.propro.algorithm.parser.LibraryTsvParser;
import net.csibio.propro.algorithm.parser.TraMLParser;
import net.csibio.propro.constants.SuccessMsg;
import net.csibio.propro.constants.enums.ResultCode;
import net.csibio.propro.constants.enums.TaskTemplate;
import net.csibio.propro.domain.ResultDO;
import net.csibio.propro.domain.db.ExperimentDO;
import net.csibio.propro.domain.db.LibraryDO;
import net.csibio.propro.domain.db.PeptideDO;
import net.csibio.propro.domain.db.TaskDO;
import net.csibio.propro.domain.query.LibraryQuery;
import net.csibio.propro.domain.query.PeptideQuery;
import net.csibio.propro.service.LibraryService;
import net.csibio.propro.service.PeptideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-05-31 09:53
 */
@Controller
@RequestMapping("library")
public class LibraryController extends BaseController {

    @Autowired
    LibraryTsvParser tsvParser;
    @Autowired
    TraMLParser traMLParser;
    @Autowired
    LibraryService libraryService;
    @Autowired
    PeptideService peptideService;

    @RequestMapping(value = "/list")
    String list(Model model,
                @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                @RequestParam(value = "searchName", required = false) String searchName,
                @RequestParam(value = "type", required = false) Integer type
                ) {
        model.addAttribute("searchName", searchName);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("type", type);
        LibraryQuery query = new LibraryQuery();
        if (searchName != null && !searchName.isEmpty()) {
            query.setName(searchName);
        }
        if (type != null){
            query.setType(type);
        }
        query.setPageSize(pageSize);
        query.setPageNo(currentPage);
        ResultDO<List<LibraryDO>> resultDO = libraryService.getList(query);

        model.addAttribute("libraryList", resultDO.getModel());
        model.addAttribute("totalPage", resultDO.getTotalPage());
        model.addAttribute("currentPage", currentPage);
        return "library/list";
    }

    @RequestMapping(value = "/create")
    String create(Model model) {
        return "library/create";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    String add(Model model,
               @RequestParam(value = "name", required = true) String name,
               @RequestParam(value = "type", required = true) Integer type,
               @RequestParam(value = "description", required = false) String description,
               @RequestParam(value = "libFile", required = true) MultipartFile libFile,
               @RequestParam(value = "prmFile", required = false) MultipartFile prmFile,
               RedirectAttributes redirectAttributes) {

        if (libFile == null || libFile.getOriginalFilename() == null || libFile.getOriginalFilename().isEmpty()) {
            model.addAttribute(ERROR_MSG, ResultCode.FILE_NOT_EXISTED);
            return "library/create";
        }
        LibraryDO library = new LibraryDO();
        library.setName(name);
        library.setDescription(description);
        library.setType(type);
        library.setFilePath(libFile.getOriginalFilename());
        ResultDO resultDO = libraryService.insert(library);
        TaskDO taskDO = new TaskDO(TaskTemplate.UPLOAD_LIBRARY_FILE, library.getName());
        taskService.insert(taskDO);
        if (resultDO.isFailed()) {
            logger.warn(resultDO.getMsgInfo());
            redirectAttributes.addFlashAttribute(ERROR_MSG, resultDO.getMsgInfo());
            redirectAttributes.addFlashAttribute("library", library);
            return "redirect://library/create";
        }
        try {
            InputStream libFileStream = libFile.getInputStream();
            InputStream prmFileStream = null;
            if (!prmFile.isEmpty()) {
                prmFileStream = prmFile.getInputStream();
            }

            libraryTask.saveLibraryTask(library, libFileStream, prmFileStream, taskDO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/task/detail/" + taskDO.getId();
    }

    @RequestMapping(value = "/aggregate/{id}")
    String aggregate(Model model, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {

        LibraryDO library = libraryService.getById(id);
        if (library == null) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, ResultCode.LIBRARY_NOT_EXISTED.getMessage());
            return "redirect:/library/list";
        }

        libraryService.countAndUpdateForLibrary(library);

        return "redirect:/library/detail/" + library.getId();
    }

    @RequestMapping(value = "/scan")
    String scan(Model model, RedirectAttributes redirectAttributes) throws FileNotFoundException {
        logger.info("开始扫描");
        libraryService.scan();
        redirectAttributes.addFlashAttribute(SUCCESS_MSG, ResultCode.OPERATING_SUCCESS.getMessage());

        return "redirect:/";
    }

    @RequestMapping(value = "/edit/{id}")
    String edit(Model model, @PathVariable("id") String id,
                RedirectAttributes redirectAttributes) {

        LibraryDO library = libraryService.getById(id);
        if (library == null) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, ResultCode.LIBRARY_NOT_EXISTED.getMessage());
            return "redirect:/library/list";
        } else {
            model.addAttribute("library", library);
            return "library/edit";
        }
    }

    @RequestMapping(value = "/detail/{id}")
    String detail(Model model, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        LibraryDO library = libraryService.getById(id);

        if (library == null) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, ResultCode.LIBRARY_NOT_EXISTED.getMessage());
            return "redirect:/library/list";
        } else {
            if (library.getType().equals(LibraryDO.TYPE_IRT)) {
                Double[] range = peptideService.getRTRange(id);
                if (range != null && range.length == 2) {
                    model.addAttribute("minRt", range[0]);
                    model.addAttribute("maxRt", range[1]);
                }
            }
            model.addAttribute("library", library);
            return "library/detail";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    String update(Model model,
                  @RequestParam(value = "id", required = true) String id,
                  @RequestParam(value = "name") String name,
                  @RequestParam(value = "type") Integer type,
                  @RequestParam(value = "description") String description,
                  @RequestParam(value = "libFile") MultipartFile libFile,
                  @RequestParam(value = "prmFile", required = false) MultipartFile prmFile,
                  RedirectAttributes redirectAttributes) {

        String redirectListUrl = null;
        if (type == 1) {
            redirectListUrl = "redirect:/library/listIrt";
        } else {
            redirectListUrl = "redirect:/library/list";
        }

        LibraryDO library = libraryService.getById(id);
        if (library == null) {
            redirectAttributes.addFlashAttribute(ERROR_MSG, ResultCode.LIBRARY_NOT_EXISTED.getMessage());
            return redirectListUrl;
        }

        library.setDescription(description);
        library.setType(type);

        ResultDO updateResult = libraryService.update(library);
        if (updateResult.isFailed()) {
            redirectAttributes.addFlashAttribute(ResultCode.UPDATE_ERROR.getMessage(), updateResult.getMsgInfo());
            return redirectListUrl;
        }

        //如果没有更新源文件,那么直接返回标准库详情页面
        if (libFile == null || libFile.getOriginalFilename() == null || libFile.getOriginalFilename().isEmpty()) {
            return "redirect:/library/detail/" + library.getId();
        }
        library.setFilePath(libFile.getOriginalFilename());
        libraryService.update(library);

        TaskDO taskDO = new TaskDO(TaskTemplate.UPLOAD_LIBRARY_FILE, library.getName());
        taskService.insert(taskDO);
        try {
            InputStream libFileStream = libFile.getInputStream();
            InputStream prmFileStream = null;
            if (!prmFile.isEmpty()) {
                prmFileStream = prmFile.getInputStream();
            }

            libraryTask.saveLibraryTask(library, libFileStream, prmFileStream, taskDO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/task/detail/" + taskDO.getId();
    }

    @RequestMapping(value = "/delete/{id}")
    String delete(Model model, @PathVariable("id") String id,
                  RedirectAttributes redirectAttributes) {
        ResultDO resultDO = libraryService.delete(id);
        peptideService.deleteAllByLibraryId(id);
        if (resultDO.isSuccess()) {
            redirectAttributes.addFlashAttribute(SUCCESS_MSG, SuccessMsg.DELETE_LIBRARY_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG, resultDO.getMsgInfo());
        }
        return "redirect:/library/list";
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    ResultDO<JSONObject> search(Model model,
                                @RequestParam(value = "fragmentSequence", required = false) String fragmentSequence,
                                @RequestParam(value = "precursorMz", required = false) Float precursorMz,
                                @RequestParam(value = "experimentId", required = false) String experimentId,
                                @RequestParam(value = "libraryId", required = false) String libraryId) {
        if (fragmentSequence.length() < 3) {
            return ResultDO.buildError(ResultCode.SEARCH_FRAGMENT_LENGTH_MUST_BIGGER_THAN_3);
        }

        LibraryDO library = libraryService.getById(libraryId);
        ResultDO<ExperimentDO> expResult = experimentService.getById(experimentId);
        if (expResult.isFailed()) {
            return ResultDO.buildError(ResultCode.EXPERIMENT_NOT_EXISTED);
        }
        ExperimentDO exp = expResult.getModel();
        List<WindowRange> rangs = exp.getWindowRanges();
        WindowRange targetRang = null;
        for (WindowRange rang : rangs) {
            if (precursorMz >= rang.getStart() && precursorMz < rang.getEnd()) {
                targetRang = rang;
                break;
            }
        }
        PeptideQuery query = new PeptideQuery();
        query.setLibraryId(libraryId);
        query.setSequence(fragmentSequence);
        if (targetRang != null) {
            query.setMzStart(Double.parseDouble(targetRang.getStart().toString()));
            query.setMzEnd(Double.parseDouble(targetRang.getEnd().toString()));
        }

        List<PeptideDO> peptides = peptideService.getAll(query);

        JSONArray peptidesArray = new JSONArray();
        for (PeptideDO peptide : peptides) {
            peptidesArray.add(peptide.getPeptideRef());
        }
        JSONObject res = new JSONObject();
        res.put("peptides", peptidesArray);
        ResultDO<JSONObject> resultDO = new ResultDO<>(true);
        resultDO.setModel(res);
        return resultDO;
    }

    @RequestMapping(value = "overview/{id}")
    @ResponseBody
    String overview(Model model, @PathVariable("id") String id) {
        LibraryDO library = libraryService.getById(id);

        List<PeptideDO> peptides = peptideService.getAllByLibraryId(id);
        int count = 0;
        for (PeptideDO peptide : peptides) {
            if (peptide.getFragmentMap().size() <= 3) {
                count++;
            }
        }
        return count + "个不符合要求的离子";
    }
}
