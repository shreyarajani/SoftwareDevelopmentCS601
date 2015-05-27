trash(emails,pages) ::= <<

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
                           <li><a href="/trash?page=$page$">$page$</a></li>
                           }$
                           </ul>
                          <br/>

                            <a href="/empty_trash">
                            <button type="button" class="btn btn-danger">Empty Trash</button></a>

                            <form class="form-inline" style="display:inline;" method="post" action="/delete_permanently" name="form">
                               <input type="submit" class="btn btn-warning" id="inbox" name="foldername" value="Delete Permanently">
                               <hr/>

                            <table class="table table-hover">
                            <tr>
                              <th width="3%"></th>
                                                            <th width="3%"></th>
                                                            <th width="20%">From <a href="/trash?fo=a-from"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/trash?fo=d-from"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                                                            <th width="35%">Subject <a href="/trash?fo=a-subject"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/trash?fo=d-subject"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                                                            <th width="15%">Date <a href="/trash?fo=a-date"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/trash?fo=d-date"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                                                            <th width="15%">Size <a href="/trash?fo=a-size"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/trash?fo=d-size"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
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