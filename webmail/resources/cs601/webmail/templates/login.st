login() ::= <<

<div class="container-fluid">
<div class="row-fluid">
                    <div class="hero-unit">
                        <form align="center" method="post" action="/process_login" name="aform" target="_top">
                        <fieldset>
                          <legend align="center">WebMail Project Login Form</legend>
                            <div align="center">Fields with * are required</div> <br/>

                            <label for="userid">Username *</label>
                               <input type="text" id="userid" name="userid" title="Please enter a valid username" required />
                            <br/><br/>
                            <label for="password">Password *</label>
                            <input type="password" id="password" name="password" required/>
                             <br/><br/>
                            </fieldset>
                            <input type="submit" class="btn btn-default" value="Sign in" />
                        <form>
                    </div>
          </div>
      </div><!--/row-->
</div><!--/.fluid-container-->

>>