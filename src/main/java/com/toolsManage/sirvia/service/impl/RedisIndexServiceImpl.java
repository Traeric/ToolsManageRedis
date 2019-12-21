package com.toolsManage.sirvia.service.impl;

import com.toolsManage.sirvia.controller.RedisIndexController;
import com.toolsManage.sirvia.service.RedisIndexService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RedisIndexServiceImpl implements RedisIndexService {
    @Override
    public Set<String> getRedisKeys() {
        return RedisIndexController.jedis.keys("*");
    }

    @Override
    public String getData(String key) {
        // 获取键的类型
        String type = RedisIndexController.jedis.type(key);
        switch (type) {
            case "string": {
                String con = RedisIndexController.jedis.get(key);
                return String.format("<div class='content'>" +
                        "<div class='left'>" +
                        "<textarea name='content' class='layui-textarea' rows='15'>%s</textarea>" +
                        "</div>" +
                        "<div class='right'>" +
                        "<button class='layui-btn' onclick='reloadString()'><i class='layui-icon'>&#xe9aa;</i> 重命名键名</button>" +
                        "<button class='layui-btn layui-btn-danger' onclick='remove()'><i class='layui-icon'>&#xe640;</i> 删除</button>" +
                        "<button class='layui-btn layui-btn-normal' onclick='saveString()'><i class='layui-icon'>&#xe608;</i> 保存</button>" +
                        "</div>" +
                        "</div>", con);
            }
            case "list":
                StringBuilder res = new StringBuilder("<div class='content'>" +
                        "<div class='left'>" +
                        "<table lay-filter='table-filter'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th lay-data=\"{field: 'select', width: 80}\">选中</th>" +
                        "<th lay-data=\"{field: 'val'}\">值</th>" +
                        "<th lay-data=\"{field: 'opt', width: 80}\">操作</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>");
                // 获取数据
                Long llen = RedisIndexController.jedis.llen(key);
                for (Long i = 0L; i < llen; i++) {
                    res.append(String.format("<tr>" +
                            "<td>" +
                            "<input type=\"checkbox\" name=\"row_select\" lay-skin=\"switch\" lay-filter=\"switchTest\" value='%s'>" +
                            "</td>" +
                            "<td>%s</td>" +
                            "<td><button type=\"button\" class=\"layui-btn layui-btn-sm layui-btn-warm\" onclick=\"modifyList(%s, '%s', this)\">修改</button>" +
                            "</td></tr>\n", i, RedisIndexController.jedis.lindex(key, i), i, RedisIndexController.jedis.lindex(key, i)));
                }
                res.append("</tbody></table></div>\n" +
                        "<div class='right'>\n" +
                        "         <button class='layui-btn' onclick='reloadString()'><i class='layui-icon'>&#xe9aa;</i> 重命名键名</button>\n" +
                        "         <button class='layui-btn layui-btn-danger' onclick='remove()'><i class='layui-icon'>&#xe640;</i> 全部删除</button>\n" +
                        "         <button class='layui-btn layui-btn-warm' onclick='removeLine()'><i class='layui-icon'>&#xe640;</i> 删除行</button>\n" +
                        "         <button class='layui-btn layui-btn-normal' onclick='addLine()'><i class='layui-icon'>&#xe608;</i> 插入行</button>\n" +
                        "    </div>\n" +
                        "</div>");
                return String.valueOf(res);
            case "hash":
                StringBuilder hash = new StringBuilder("<div class='content'>" +
                        "<div class='left'>" +
                        "<table lay-filter='table-filter'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th lay-data=\"{field: 'select', width: 80}\">选中</th>" +
                        "<th lay-data=\"{field: 'key'}\">键名</th>" +
                        "<th lay-data=\"{field: 'value'}\">值</th>" +
                        "<th lay-data=\"{field: 'opt', width: 80}\">操作</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>");
                // 获取数据
                Map<String, String> stringStringMap = RedisIndexController.jedis.hgetAll(key);
                stringStringMap.forEach((k, v) -> {
                    hash.append(String.format("<tr>" +
                            "<td>" +
                            "<input type=\"checkbox\" name=\"row_select\" lay-skin=\"switch\" lay-filter=\"switchTest\" value='%s'>" +
                            "</td>" +
                            "<td>%s</td>\n" +
                            "<td>%s</td>" +
                            "<td><button type=\"button\" class=\"layui-btn layui-btn-sm layui-btn-warm\" onclick=\"modifyHash('%s', '%s', this)\">修改</button>" +
                            "</td></tr>", k, k, v, k, v));
                });
                hash.append("</tbody></table></div>\n" +
                        "<div class='right'>\n" +
                        "         <button class='layui-btn' onclick='reloadString()'><i class='layui-icon'>&#xe9aa;</i> 重命名键名</button>\n" +
                        "         <button class='layui-btn layui-btn-danger' onclick='remove()'><i class='layui-icon'>&#xe640;</i> 全部删除</button>\n" +
                        "         <button class='layui-btn layui-btn-warm' onclick='removeLineHash()'><i class='layui-icon'>&#xe640;</i> 删除行</button>\n" +
                        "         <button class='layui-btn layui-btn-normal' onclick='addLineHash()'><i class='layui-icon'>&#xe608;</i> 插入行</button>\n" +
                        "    </div>\n" +
                        "</div>");
                return String.valueOf(hash);
            case "zset":
                StringBuilder zset = new StringBuilder("<div class='content'>" +
                        "<div class='left'>" +
                        "<table lay-filter='table-filter'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th lay-data=\"{field: 'select', width: 80}\">选中</th>" +
                        "<th lay-data=\"{field: 'val'}\">值</th>" +
                        "<th lay-data=\"{field: 'score', width: 80}\">分数</th>" +
                        "<th lay-data=\"{field: 'opt', width: 80}\">操作</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>");
                // 获取数据
                Set<String> zrange = RedisIndexController.jedis.zrange(key, 0, -1);
                for (String member : zrange) {
                    zset.append(String.format("<tr>" +
                            "<td>" +
                            "<input type=\"checkbox\" name=\"row_select\" lay-skin=\"switch\" lay-filter=\"switchTest\" value='%s'>" +
                            "</td>" +
                            "<td>%s</td>\n" +
                            "<td>%s</td>" +
                            "<td><button type=\"button\" class=\"layui-btn layui-btn-sm layui-btn-warm\" onclick=\"modifyZset('%s', '%s', this)\">修改</button>" +
                            "</td></tr>", member, member, RedisIndexController.jedis.zscore(key, member), member, RedisIndexController.jedis.zscore(key, member)));
                }
                zset.append("</tbody></table></div>\n" +
                        "<div class='right'>\n" +
                        "         <button class='layui-btn' onclick='reloadString()'><i class='layui-icon'>&#xe9aa;</i> 重命名键名</button>\n" +
                        "         <button class='layui-btn layui-btn-danger' onclick='remove()'><i class='layui-icon'>&#xe640;</i> 全部删除</button>\n" +
                        "         <button class='layui-btn layui-btn-warm' onclick='removeLineZset()'><i class='layui-icon'>&#xe640;</i> 删除行</button>\n" +
                        "         <button class='layui-btn layui-btn-normal' onclick='addLineZset()'><i class='layui-icon'>&#xe608;</i> 插入行</button>\n" +
                        "    </div>\n" +
                        "</div>");
                return String.valueOf(zset);
            case "set":
                StringBuilder set = new StringBuilder("<div class='content'>" +
                        "<div class='left'>" +
                        "<table lay-filter='table-filter'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th lay-data=\"{field: 'select', width: 80}\">选中</th>" +
                        "<th lay-data=\"{field: 'val'}\">值</th>" +
                        "<th lay-data=\"{field: 'opt', width: 80}\">操作</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>");
                // 获取数据
                Set<String> smembers = RedisIndexController.jedis.smembers(key);
                for (String member : smembers) {
                    set.append(String.format("<tr>" +
                            "<td>" +
                            "<input type=\"checkbox\" name=\"row_select\" lay-skin=\"switch\" lay-filter=\"switchTest\" value='%s'>" +
                            "</td>" +
                            "<td>%s</td>" +
                            "<td><button type=\"button\" class=\"layui-btn layui-btn-sm layui-btn-warm\" onclick=\"modifySet('%s', this)\">修改</button>" +
                            "</td></tr>\n", member, member, member));
                }
                set.append("</tbody></table></div>\n" +
                        "<div class='right'>\n" +
                        "         <button class='layui-btn' onclick='reloadString()'><i class='layui-icon'>&#xe9aa;</i> 重命名键名</button>\n" +
                        "         <button class='layui-btn layui-btn-danger' onclick='remove()'><i class='layui-icon'>&#xe640;</i> 全部删除</button>\n" +
                        "         <button class='layui-btn layui-btn-warm' onclick='removeLineSet()'><i class='layui-icon'>&#xe640;</i> 删除行</button>\n" +
                        "         <button class='layui-btn layui-btn-normal' onclick='addLineSet()'><i class='layui-icon'>&#xe608;</i> 插入行</button>\n" +
                        "    </div>\n" +
                        "</div>");
                return String.valueOf(set);
            default:
                break;
        }
        return null;
    }
}
