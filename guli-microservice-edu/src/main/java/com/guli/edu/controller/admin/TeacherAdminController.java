package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.edu.entity.Teacher;
import com.guli.edu.query.TeacherQuery;
import com.guli.edu.service.TeacherService;
import com.guli.common.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//定义在类上：@Api
//定义在方法上：@ApiOperation
//定义在参数上：@ApiParam

@RestController //统一定义所有的方法返回值是Json
@RequestMapping("/admin/edu/teacher")
@CrossOrigin //跨域
@Api(description = "讲师管理")
public class TeacherAdminController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping     //需通过get方式请求才可以使用list方法
    @ApiOperation(value = "所有讲师列表")
    public R list() {
        List<Teacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "根据id删除讲师")
    public R removeById(@ApiParam(name = "id", value = "讲师ID", required = true)
                        @PathVariable String id) {
        teacherService.removeById(id);
        return R.ok().message("删除成功!");
    }

//    @ApiOperation(value = "分页讲师列表")
//    @GetMapping("{page}/{limit}")
//    public R pageList(
//            @ApiParam(name = "page", value = "当前页码", required = true)
//            @PathVariable Long page,
//
//            @ApiParam(name = "limit", value = "每页记录数", required = true)
//            @PathVariable Long limit){
//
//        Page<Teacher> pageParam = new Page<>(page, limit);
//
//        teacherService.page(pageParam, null);
//        List<Teacher> records = pageParam.getRecords();
//        long total = pageParam.getTotal();
//
//        return  R.ok().data("total", total).data("rows", records);
//    }

    @ApiOperation(value = "根据条件查询并分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "teacherquery", value = "查询对象", required = false)
                    TeacherQuery teacherquery) {

        Page<Teacher> pageParam = new Page<>(page, limit);

        teacherService.pageQuery(pageParam, teacherquery);
        List<Teacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        if (page <= 0 || limit <= 0) {
            //throw new GuliException(21003, "参数不正确1");
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        } else return R.ok().data("total", total).data("rows", records);
    }

    @PostMapping
    @ApiOperation(value = "添加教师")
    public R insertNew(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody Teacher teacher) { //当前端返回的是JSON数据类型时，参数必须加注解RequestBody
        teacherService.save(teacher);
        return R.ok();
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询教师")
    public R selectById(
            @ApiParam(name = "id", value = "查询教师的id", required = true)
            @PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        return R.ok().data("item", teacher);
    }


    @PutMapping("{id}")
    @ApiOperation(value = "根据id修改教师")
    public R updateById(@ApiParam(name = "id", value = "所要修改教师的id", required = true)
                        @PathVariable String id,
                        @ApiParam(name = "teacher", value = "讲师对象", required = true)
                        @RequestBody Teacher teacher) {
        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok();
    }
}
