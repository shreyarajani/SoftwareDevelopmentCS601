register() ::= <<
<body>
<div class="container-fluid">
<div class="row-fluid">
                    <div class="hero-unit">
                        <form align="center" method="post" action="/process_register" name="form" target="_top">
                        <fieldset>
                          <legend align="center">WebMail Project Registration Form</legend>
                            <div align="center">Fields with * are required</div> <br/>

                               <label for="firstname">First Name *</label>
                               <input type="text" id="firstname" name="firstname" placeholder="First Name" required />
                               <br/><br/>
                               <label for="lastname">Last Name *</label>
                               <input type="text" id="lastname" name="lastname" placeholder="Last Name" required />
                                <br/><br/>
                               <label for="userid">Username *</label>
                               <input type="text" id="userid" name="userid" title="Please enter a valid username" required />
                                <br/><br/>
                            <label for="password">Password *</label>
                            <input type="password" id="password" name="password" title="Minimum 8 characters" required/>
                            <br/>
                            </fieldset>
                            <input type="submit" class="btn btn-default" value="Sign up" />
                        <form>
                        <br/><br/>

                        Already have an account? <br/>
                        <a href="/login">Login</a>
                    </div>
          </div>
      </div><!--/row-->
</div><!--/.fluid-container-->
</body>
>>