package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.GroupEntity;
import com.capstone.LEMS.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class GroupController {

    @Autowired
    GroupService gserv;

    @PostMapping("/insertgroup")
    public GroupEntity addGroup(@RequestBody GroupEntity group){
        return gserv.addGroup(group);
    }
    @GetMapping("/getallgroup")
    public List<GroupEntity> getAllGroup(){
        return gserv.getAllGroup();
    }
   @PutMapping("/updategroup/{group_id}")
   public GroupEntity updateGroup(@RequestBody GroupEntity group, @PathVariable int group_id){
        return gserv.updateGroup(group,group_id);
   }
   @DeleteMapping("/deleteGroup/{group_id}")
    public void deleteGroup(@PathVariable int group_id){
          gserv.deleteGroup(group_id);
    }

}
