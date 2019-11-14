package com.zgdj.lib.bean;

import java.io.Serializable;

public class AuthorityBean implements Serializable {

    /**
     * path : /admin/group/editNode
     * authority : 1
     * title : 新增编辑组织机构树节点
     */

    private String path;
    private int authority;
    private String title;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
