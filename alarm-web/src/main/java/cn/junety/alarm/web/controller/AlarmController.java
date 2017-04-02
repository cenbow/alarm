package cn.junety.alarm.web.controller;

import cn.junety.alarm.base.entity.*;
import cn.junety.alarm.web.common.ResponseHelper;
import cn.junety.alarm.web.service.AlarmService;
import cn.junety.alarm.web.service.GroupService;
import cn.junety.alarm.web.service.ProjectService;
import cn.junety.alarm.web.vo.AlarmForm;
import cn.junety.alarm.web.vo.AlarmVO;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Created by caijt on 2017/3/24.
 */
@Controller
public class AlarmController extends BaseController {

    @Autowired
    private AlarmService alarmService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    public ModelAndView toAlarmPage(HttpServletRequest request, Model model) {
        User user = getUser(request);
        logger.info("GET /alarm, user:{}", JSON.toJSONString(user));

        model.addAttribute("user", user);

        return new ModelAndView("alarm");
    }

    @ResponseBody
    @RequestMapping(value = "/alarms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAlarms(HttpServletRequest request) {
        User user = getUser(request);
        AlarmForm alarmForm = new AlarmForm(request);
        logger.info("GET /alarms, user:{}, body:{}", JSON.toJSONString(user), JSON.toJSONString(alarmForm));

        List<AlarmVO> alarms = alarmService.getAlarmInfo(user, alarmForm);
        int count = alarmService.getAlarmInfoCount(user, alarmForm);

        return ResponseHelper.buildResponse(2000, "alarms", alarms, "count", count);
    }

    @ResponseBody
    @RequestMapping(value = "/alarms/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCreateInfo(HttpServletRequest request) {
        User user = getUser(request);
        logger.info("GET /alarms/info, user:{}", JSON.toJSONString(user));

        List<Integer> codes = alarmService.getAlarmCodeList(user);
        List<Project> projects = projectService.getProjectList();
        List<Module> modules;
        if (projects.size() > 0) {
            modules = projectService.getModuleByPid(projects.get(0).getId());
        } else {
            modules = Collections.emptyList();
        }
        List<Group> groups = groupService.getAllGroup();

        return ResponseHelper.buildResponse(2000, "codes", codes, "projects", projects,
                "modules", modules, "groups", groups);
    }

    //// 获取指定id的告警
    //@ResponseBody
    //@RequestMapping(value = "/alarms/{aid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    //public String getAlarmById(@PathVariable Integer aid) {
    //    logger.info("GET /alarms/{}", aid);
    //    Map<String, Object> results = new HashMap<>();
    //    results.put("codes", alarmService.getCodes());
    //    List<Project> projects = alarmService.getProjects();
    //    Alarm alarm = alarmService.getAlarmById(aid);
    //    results.put("projects", projects);
    //    results.put("modules", alarmService.getModules(alarm.getProjectId()));
    //    results.put("groups", alarmService.getGroups());
    //    results.put("alarm", alarm);
    //    return ResponseHelper.buildResponse(2000, results);
    //}
//
    //@ResponseBody
    //@RequestMapping(value = "/alarms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public String addAlarm(@RequestBody Alarm alarm) {
    //    logger.info("POST /alarms, body:{}", alarm);
    //    alarmService.createAlarm(alarm);
    //    return ResponseHelper.buildResponse(2000, "success");
    //}
//
    //@ResponseBody
    //@RequestMapping(value = "/alarms", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    //public String updateAlarm(@RequestBody Alarm alarm) {
    //    logger.info("PUT /alarms, body:{}", JSON.toJSONString(alarm));
    //    alarmService.updateAlarm(alarm);
    //    return ResponseHelper.buildResponse(2000, "success");
    //}
//
    //@ResponseBody
    //@RequestMapping(value = "/alarms/{aid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    //public String deleteAlarm(@PathVariable Integer aid) {
    //    logger.info("DELETE /alarms/{}", aid);
    //    alarmService.deleteAlarmById(aid);
    //    return ResponseHelper.buildResponse(2000, "success");
    //}
}
