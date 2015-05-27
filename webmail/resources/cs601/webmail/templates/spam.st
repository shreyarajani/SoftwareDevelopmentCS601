spam(emails,pages) ::= <<

$mailboxTop()$

                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          <div align="center">
                          <b>SPAM</b>
                          <div>
                          <ul class="pagination">
                             $pages:{page|
                              <li><a href="/spam?page=$page$">$page$</a></li>
                            }$
                           </ul>
                          <br/>

                            <form class="form-inline" style="display:inline;" method="post" action="/move_to_folder" name="form">
                               <input type="submit" class="btn btn-default" id="inbox" name="foldername" value="Not Spam">
                               <hr/>

                            <table class="table table-hover">
                            <tr>
                              <th width="3%"></th>
                                                            <th width="3%"></th>
                                                            <th width="20%">From <a href="/spam?fo=a-from"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/spam?fo=d-from"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                                                            <th width="35%">Subject <a href="/spam?fo=a-subject"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/spam?fo=d-subject"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                                                            <th width="15%">Date <a href="/spam?fo=a-date"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/spam?fo=d-date"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                                                            <th width="15%">Size <a href="/spam?fo=a-size"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/spam?fo=d-size"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                                                            <th width="15%">View</th>
                            </tr>
                            $emails:{email|
                              <tr>
                                <td>
                                <div class="checkbox" id="check">
                                  <input type="checkbox" name="mailid" value="$email.mailid$">
                                </div>
                                </td>
                                <td><a href="/toggle?id=$email.mailid$&f=$email.folder$">$email.maildisplay$</a></td>
                                <td>$email.from$</td>
                                <td>$email.subject$</td>
                                <td>$email.date$</td>
                                <td>$email.size$</td></a>
                                <td><a href="/viewemail?id=$email.mailid$">View</a>
                              </tr>
                              }$
                            </table>
                            </form>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>