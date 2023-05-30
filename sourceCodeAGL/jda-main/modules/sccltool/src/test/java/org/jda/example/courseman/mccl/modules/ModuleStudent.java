package org.jda.example.courseman.mccl.modules;

import java.util.Collection;

import org.jda.example.courseman.bspacegen.output.Address;
import org.jda.example.courseman.bspacegen.output.Enrolment;

import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.view.AttributeDesc;

@ModuleDescriptor(name = "ModuleStudent", modelDesc = @jda.modules.mccl.syntax.model.ModelDesc(model = org.jda.example.courseman.bspacegen.output.Student.class), viewDesc = @jda.modules.mccl.syntax.view.ViewDesc(formTitle = "Form: Student", imageIcon = "Student.png", domainClassLabel = "Student", view = jda.mosa.view.View.class), controllerDesc = @jda.modules.mccl.syntax.controller.ControllerDesc())
public class ModuleStudent {

    @AttributeDesc(label = "title")
    private String title;

    @AttributeDesc(label = "id")
    private int id;

    @AttributeDesc(label = "name")
    private String name;

    @AttributeDesc(label = "address")
    private Address address;

    @AttributeDesc(label = "enrolments")
    private Collection<Enrolment> enrolments;
}
