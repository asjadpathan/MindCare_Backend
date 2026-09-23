package com.MindCare.dto;

public class SessionBooking {
    private Long doctorId;
    private String time;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    // remove userEmail if you're using Spring Security context
}
