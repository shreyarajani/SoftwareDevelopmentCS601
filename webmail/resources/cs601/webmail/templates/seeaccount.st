seeaccount(account) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">

                            <b>Account details for : $account.account$</b><br/>
                            <br/>
                            <b>POP Host:</b> $account.popserver$
                            <br/>
                            <b>POP Port: </b>$account.popport$
                            <br/>
                            <b>SMTP Host: </b>$account.smtpserver$
                            <br/>
                            <b>SMTP Port: </b>$account.smtpport$

                            <br/><br/>
                            <font color="red">
                            You cannot make changes to the account. You can <a href="/deleteaccount?id=$account.account$"> Delete</a> or <a href="/loadaccount?id=$account.account$">Load Account</a> only!
                            </font>
                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>