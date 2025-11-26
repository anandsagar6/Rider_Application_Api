package com.rideer.backend.model;

public enum DocumentType {
    PROFILE("profile.jpg"),
    DL_FRONT("dl_front.jpg"),
    DL_BACK("dl_back.jpg"),
    AADHAAR_FRONT("aadhar_front.jpg"),
    AADHAAR_BACK("aadhar_back.jpg"),
    VEHICLE("vehicle.jpg");

    private final String fileName;

    DocumentType(String fileName) {
        this.fileName = fileName;
    }

    public String toFileName() {
        return fileName;
    }
}
