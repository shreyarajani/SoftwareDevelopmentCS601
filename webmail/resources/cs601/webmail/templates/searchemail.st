searchemail(emails) ::= <<

$mailboxTop()$

                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          <div align="center">
                          <b>Search Results</b>
                          <div>
                          <br/>
                            <font size="3">

                            <table class="table table-hover">
                            <tr>
                              <th width="3%"></th>
                              <th width="3%"></th>
                              <th width="25%">From</th>
                              <th width="35%">Subject</th>
                              <th width="30%">Date</th>
                              <th width="10%">Size</th>
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

                          </font>
                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>