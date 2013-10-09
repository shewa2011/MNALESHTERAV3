package com.mnaleshterav3.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;
 







import javax.servlet.http.HttpServletResponse;
 







import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;



import com.mnaleshterav3.model.Document;
import com.mnaleshterav3.model.Owner;
 
@Controller
public class DocumentController {
	public static InputStream in;
	public static long fs;
     
    @Autowired
    private com.mnaleshterav3.dao.DocumentDAO documentDao;
    @RequestMapping(value="/home")
    public ModelAndView home(){
    	return new ModelAndView("home");
    }
    
    @RequestMapping("/register")
    public String listContacts(Map<String, Object> map) {
 
        map.put("contact", new Owner());
       // map.put("contactList", contactService.listContact());
 
        return "register";
    }
 
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView addContact(@ModelAttribute("contact")
    Owner owner, BindingResult result) {
    	ModelAndView mav=new ModelAndView("owner");
    	
    	String message = "YOU ARE SUCCESSFULLY REGISTERED.";
        mav.addObject("message", message);
 
      documentDao.addOwner(owner);
 
        return mav;
    }
        /*String message = "Team was successfully added.";
        modelAndView.addObject("message", message);
        return modelAndView; 
    }  */
       
     
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        try {
            map.put("document", new Document());
            map.put("documentList", documentDao.list());
        }catch(Exception e) {
            e.printStackTrace();
        }
 
        return "documents";
    }
 
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(
            @ModelAttribute("document") Document document,
            @RequestParam("file") MultipartFile file) {
         
         
        System.out.println("Name:" + document.getName());
        System.out.println("Desc:" + document.getDescription());
        System.out.println("File:" + file.getName());
        System.out.println("ContentType:" + file.getContentType());
         
        try {
            in = file.getInputStream(); //File being uploaded 
        fs=file.getSize();
        
           
           // Hibernate.getLobCreator(sessionFactory().getCurrentSession()).createBlob(file.getInputStream(),file.getSize());
 
            document.setFilename(file.getOriginalFilename());
            
            document.setContentType(file.getContentType());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        try {
            documentDao.save(document);
        } catch(Exception e) {
            e.printStackTrace();
        }
         
        return "redirect:/index.html";
    }
 
    @RequestMapping("/download/{documentId}")
    public String download(@PathVariable("documentId")
            Integer documentId, HttpServletResponse response) {
         
        Document doc = documentDao.get(documentId);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +doc.getFilename()+ "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType(doc.getContentType());
            IOUtils.copy(doc.getContent().getBinaryStream(), out);
            out.flush();
            out.close();
         
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
         
         
        return null;
    }
 
    @RequestMapping("/remove/{documentId}")
    public String remove(@PathVariable("documentId")
            Integer documentId) {
         
        documentDao.remove(documentId);
         
        return "redirect:/index.html";
    }
}