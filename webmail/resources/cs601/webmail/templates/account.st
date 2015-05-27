account() ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">

                            <form align="center" method="post" action="/process_account" name="form" target="_top">
                                                    <fieldset>
                                                      <legend align="center">Account Setting</legend>
                                                        <div align="center">All fields are required</div> <br/>

                                                           <label for="email">Email</label>
                                                           <input type="text" id="email" name="email" placeholder="Email" required />

                                                           <label for="password">Password</label>
                                                           <input type="password" id="password" name="password" required />

                                                           <label for="popserver">POP Server</label>
                                                           <input type="text" id="popserver" name="popserver" placeholder="pop.domain.com" title="Please enter a valid POP server" required />

                                                        <label for="popport">POP Port</label>
                                                        <input type="text" id="popport" name="popport" placeholder="995" title="Please enter a valid POP port" required />

                                                         <label for="smtpserver">SMTP Server</label>
                                                         <input type="text" id="smtpserver" name="smtpserver" placeholder="smtp.domain.com" title="Please enter a valid SMTP server" required />

                                                         <label for="smtpport">SMTP Port</label>
                                                         <input type="text" id="smtpport" name="smtpport" placeholder="465" title="Please enter a valid SMTP port" required />

                                                        </fieldset>
                                                        <input type="submit" value="Sign up" />
                                                    <form>
                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>