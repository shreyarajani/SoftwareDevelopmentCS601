inbox(emails, folders, pages) ::= <<

$mailboxTop()$

                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          <div align="center">
                          <b>INBOX</b>
                          <div>
                          <ul class="pagination">
                          $pages:{page|
                            <li><a href="/inbox?page=$page$">$page$</a></li>
                           }$
                          </ul>
                          <br/>

                            <form class="form-inline" style="display:inline;" method="post" action="/move_to_folder" name="form">
                              <select name="foldername" required id="foldername">
                               <option value="" style="display:none"></option>
                               <option disabled="disabled" selected="selected">Move to Folder</option>
                               <option value="inbox_inbox" id="inbox_inbox">Inbox</option>
                               <option value="inbox_spam" id="inbox_spam">Spam</option>
                               <option value="inbox_trash" id="inbox_trash">Trash</option>
                               $folders:{folder|
                               <option value="inbox_$folder.folder$" id="inbox_$folder.folder$">$folder.folder$</option>
                               }$

                               </select>
                               <button type="submit" class="btn btn-default">Move</button>
                               <hr/>

                            <table class="table table-hover">
                            <tr>
                              <th width="3%"></th>
                              <th width="3%"></th>
                              <th width="20%">From <a href="/inbox?fo=a-from"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/inbox?fo=d-from"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                              <th width="35%">Subject <a href="/inbox?fo=a-subject"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/inbox?fo=d-subject"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                              <th width="15%">Date <a href="/inbox?fo=a-date"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/inbox?fo=d-date"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                              <th width="15%">Size <a href="/inbox?fo=a-size"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/inbox?fo=d-size"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                              <th width="15%">View</th>
                            </tr>
                            $emails:{email|
                              <tr>
                                <td>
                                <div class="checkbox" required id="check" >
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