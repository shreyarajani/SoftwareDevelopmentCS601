changepassword() ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          Change Password: <br/>
                      <form align="center" method="post" action="/change_password" name="form" target="_top">
                        <fieldset>
                          <legend align="center">Change Password</legend>

                               <label for="currentpassword">Current Password</label>
                               <input type="password" id="currentpassword" name="currentpassword" title="Minimum 8 characters" required />

                               <label for="newpassword">New Password</label>
                               <input type="password" id="newpassword" name="newpassword" title="Minimum 8 characters" required />

                            </fieldset>
                            <input type="submit" value="Change Password" />
                        <form>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>