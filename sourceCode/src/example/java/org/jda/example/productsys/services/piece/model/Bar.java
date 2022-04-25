package org.jda.example.productsys.services.piece.model;

import org.jda.example.productsys.services.conveyor.model.Conveyor;

import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.dcsl.syntax.AttrRef;
import jda.modules.dcsl.syntax.DOpt;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public class Bar extends Piece {

    @DOpt(type = DOpt.Type.DataSourceConstructor, requires = "", effects = "")
    public Bar(@AttrRef(value = "id") Integer id, @AttrRef(value = "conveyor") Conveyor conveyor) throws ConstraintViolationException {
        super(id, conveyor);
    }

    @DOpt(type = DOpt.Type.ObjectFormConstructor, requires = "", effects = "")
    @DOpt(type = DOpt.Type.RequiredConstructor)
    public Bar(@AttrRef(value = "conveyor") Conveyor conveyor) throws ConstraintViolationException {
        super(conveyor);
    }
}
