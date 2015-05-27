selectaccount(accounts) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          Select account: <br/>

                            $accounts:{account|
                            <a href="/seeaccount?id=$account.account$"> $account.account$</a> &nbsp&nbsp&nbsp&nbsp
                            <a href="/loadaccount?id=$account.account$">Load Account</a> &nbsp&nbsp&nbsp&nbsp
                            <br/>
                            }$
                            <br/>
                            <a href="/account">Add Account</a>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>