package com.ramostear.application.controller;

import com.ramostear.application.model.FileInfo;
import com.ramostear.application.util.FileUtil;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.jms.*;
import javax.naming.InitialContext;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : ramostear
 * @date : 2019/3/8 0008-15:35
 */
@Controller
public class FileController {

    private static String fileUploadRootDir = null;
    private IVCompressor compressor = null;

    @Value ( "${file.upload.root.dir.windows}" )
    String fileUploadRootDirWindows;

    @Value ( "${file.upload.root.dir.mac}" )
    String fileUploadRootDirMac;
    //  l

    @Value ( "${file.upload.root.dir.linux}" )
    String fileUploadRootDirLinux;

    private static Map<String,FileInfo> fileRepository = new HashMap<>();
    private static Map<String,FileInfo> fileRepository_xixi = new HashMap<>();

    @PostConstruct
    public void initFileRepository(){
//        FileInfo file1 = new FileInfo ().setFileName ( "bg1.jpg" );
//        FileInfo file2 = new FileInfo ().setFileName ( "bg2.jpg" );
//        FileInfo file3 = new FileInfo ().setFileName ( "bg3.jpg" );
//        fileRepository.put ( file1.getName (),file1 );
//        fileRepository.put ( file2.getName (),file2 );
//        fileRepository.put ( file3.getName (),file3 );

        compressor = new IVCompressor();

        // 判断文件夹是否存在，不存在就创建
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            // 苹果
            System.out.println("this is mac system");
            fileUploadRootDir = fileUploadRootDirMac;
        } else if (osName.startsWith("Windows")) {
            // windows
            System.out.println("this is windows system");
            fileUploadRootDir = fileUploadRootDirWindows;
        } else {
            // unix or linux
            System.out.println("this is linux system");
            fileUploadRootDir = fileUploadRootDirLinux;
        }
        // if it already has the dir
        if( !FileUtil.createDirectories(fileUploadRootDir))
        {
            System.out.println("need code fix");
        }
    }

    @GetMapping("/files")
    public String files(Model model){
        Collection<FileInfo> files = fileRepository.values ();
        model.addAttribute ( "data",files );
        return "files";
    }
    @GetMapping("/refresh_files")
    public String refresh_files(Model model){
        System.out.println("refresh_files");
        fileRepository.putAll(fileRepository_xixi);
        Collection<FileInfo> files = fileRepository.values ();
        model.addAttribute ( "data",files );
        return "files";
    }


    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        // 获取上传文件
        File convertFile = new File ( fileUploadRootDir+file.getOriginalFilename ());
        FileOutputStream fileOutputStream = new FileOutputStream ( convertFile );
        fileOutputStream.write ( file.getBytes () );

        notify_message(file.getOriginalFilename());
//        // 转换上传文件为两种格式
//        try {
//            byte[] arr = compressor.reduceVideoSize(file.getBytes (), VideoFormats.MP4, ResizeResolution.R360P);
//            saveFile("360P_"+file.getOriginalFilename (),arr);
//
//            byte[] arr1 = compressor.reduceVideoSize(file.getBytes (), VideoFormats.MP4, ResizeResolution.R720P);
//            saveFile("720P_"+file.getOriginalFilename (),arr1);
//        } catch (VideoException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        fileOutputStream.close ();



        FileInfo fileInfo = new FileInfo()
                .setFileName ( file.getOriginalFilename());

        FileInfo fileInfo1 = new FileInfo()
                .setFileName ( "360P_"+file.getOriginalFilename ());

        FileInfo fileInfo2 = new FileInfo()
                .setFileName ( "720P_"+file.getOriginalFilename ());

        fileRepository.put ( fileInfo.getName (),fileInfo);
        fileRepository_xixi.put ( "360P_"+file.getOriginalFilename (),fileInfo1);
        fileRepository_xixi.put ( "720P_"+file.getOriginalFilename (),fileInfo2);

        return "File is upload successfully";
    }

    @GetMapping("/download/{fileName}")
    @ResponseBody
    public ResponseEntity<Object> downloadFile(@PathVariable(name = "fileName") String fileName) throws FileNotFoundException {

        File file = new File ( fileUploadRootDir+fileName);
        InputStreamResource resource = new InputStreamResource ( new FileInputStream ( file ) );

        HttpHeaders headers = new HttpHeaders();
        headers.add ( "Content-Disposition",String.format("attachment;filename=\"%s",fileName));
        headers.add ( "Cache-Control","no-cache,no-store,must-revalidate" );
        headers.add ( "Pragma","no-cache" );
        headers.add ( "Expires","0" );

        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers ( headers )
                .contentLength ( file.length ())
                .contentType(MediaType.parseMediaType ( "application/txt" ))
                .body(resource);

        return responseEntity;
    }

    public static void saveFile(String filename,byte [] data)throws Exception{
        if(data != null){
            String filepath = fileUploadRootDir + filename;
            File file  = new File(filepath);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data,0,data.length);
            fos.flush();
            fos.close();
        }
    }
    public static void notify_message( String message)
    {
        try
        {   //Create and start connection
            InitialContext ctx=new InitialContext();
            QueueConnectionFactory f=(QueueConnectionFactory)ctx.lookup("myQueueConnectionFactory");
            QueueConnection con=f.createQueueConnection();
            con.start();
            //2) create queue session
            QueueSession ses=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            //3) get the Queue object
            Queue t=(Queue)ctx.lookup("myQueue");
            //4)create QueueSender object
            QueueSender sender=ses.createSender(t);
            //5) create TextMessage object
            TextMessage msg=ses.createTextMessage();

            //6) write message

            msg.setText(message);
            sender.send(msg);

            //8) connection close
            con.close();
        }catch(Exception e){System.out.println(e);}
    }



}
