package controllers.admin;

import controllers.CRUD;
import controllers.Check;
import controllers.SecureLinkIt;
import models.Member;
import models.Role;
import play.mvc.With;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Check({Role.ADMIN_MEMBER, Role.ADMIN_PLANNING, Role.ADMIN_SESSION, Role.ADMIN_SPEAKER})
@With(SecureLinkIt.class)
@CRUD.For(Member.class)
public class MembersAdmin extends CRUD
{
}
