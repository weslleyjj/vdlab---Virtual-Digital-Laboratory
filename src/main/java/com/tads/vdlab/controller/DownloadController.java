package com.tads.vdlab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @Value("${arquivos.pdf}")
    private String localDosPdfs;

    @Value("${arquivos.libs}")
    private String localDasLibs;

    @RequestMapping(value = "/pinagem-de2", method = RequestMethod.GET, produces = {MediaType.APPLICATION_PDF_VALUE})
    @ResponseBody
    public byte[] getFileMapeamentoDE2() throws IOException {
        FileSystemResource fs = new FileSystemResource(localDosPdfs+"DE2_Pin_Table.pdf");
        InputStream in = fs.getInputStream();
        return in.readAllBytes();
    }

    @RequestMapping(value = "/vdlab-lib-download", method = RequestMethod.GET, produces = {MediaType.MULTIPART_MIXED_VALUE})
    @ResponseBody
    public byte[] getLibVDLAB() throws IOException {
        FileSystemResource fs = new FileSystemResource(localDasLibs+"vdlab_lib.zip");
        InputStream in = fs.getInputStream();
        return in.readAllBytes();
    }
}
