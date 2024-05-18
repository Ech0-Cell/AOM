package org.mth3902.aom.controller;

import org.mth3902.aom.model.Package;
import org.mth3902.aom.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "api/package")
public class PackageController {

    private final PackageRepository packageRepository;

    @Autowired
    public PackageController(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @GetMapping
    public ArrayList<Package> getAllPackages() throws Exception {
        return packageRepository.getAllPackages();
    }
}
