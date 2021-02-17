package com.koreait.gumzimaregist.register;

public class Member {
    private int member_id;
    private String m_id;
    private String m_pass;
    private String m_name;
    private String m_email;
    private String m_phone;
    private String m_addr1;
    private String m_addr2;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getM_pass() {
        return m_pass;
    }

    public void setM_pass(String m_pass) {
        this.m_pass = m_pass;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_email() {
        return m_email;
    }

    public void setM_email(String m_email) {
        this.m_email = m_email;
    }

    public String getM_phone() {
        return m_phone;
    }

    public void setM_phone(String m_phone) {
        this.m_phone = m_phone;
    }

    public String getM_addr1() {
        return m_addr1;
    }

    public void setM_addr1(String m_addr1) {
        this.m_addr1 = m_addr1;
    }

    public String getM_addr2() {
        return m_addr2;
    }

    public void setM_addr2(String m_addr2) {
        this.m_addr2 = m_addr2;
    }
}
