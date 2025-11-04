window.LoginPlugin = function () {
  return {
    components: {
      Topbar: (Original, system) => () => {
        const handleLoginClick = () => {
          const modal = document.createElement("div");
          modal.innerHTML = `
            <div id="loginModal" style="
              position: fixed;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              background: rgba(0,0,0,0.6);
              display: flex;
              justify-content: center;
              align-items: center;
              z-index: 9999;
            ">
              <div style="
                background: white;
                padding: 20px;
                border-radius: 10px;
                width: 300px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.3);
              ">
                <h4 style="text-align:center;"> Iniciar Sesión</h4>
                <div style="margin-top:10px;">
                  <label>Usuario</label>
                  <input id="loginUser" type="text" style="width:100%; padding:5px; margin-bottom:10px;" placeholder="admin">
                  <label>Contraseña</label>
                  <input id="loginPass" type="password" style="width:100%; padding:5px;" placeholder="1234">
                </div>
                <div style="margin-top:15px; text-align:center;">
                  <button id="btnLoginConfirm" style="background:#1e88e5; color:white; border:none; padding:6px 10px; border-radius:5px;">Entrar</button>
                  <button id="btnCancel" style="background:#ccc; border:none; padding:6px 10px; border-radius:5px; margin-left:5px;">Cancelar</button>
                </div>
              </div>
            </div>
          `;

          document.body.appendChild(modal);

          document.getElementById("btnCancel").onclick = () => modal.remove();

          document.getElementById("btnLoginConfirm").onclick = async () => {
            const username = document.getElementById("loginUser").value;
            const password = document.getElementById("loginPass").value;

            const response = await fetch("/api/auth/login", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({ username, password }),
            });

            const data = await response.json();

            if (response.ok) {
              const token = "Bearer " + data.token;
              system.authActions.authorize({
                bearerAuth: { value: token },
              });
              alert(" Token generado y aplicado correctamente");
              modal.remove();
            } else {
              alert(" Error: " + (data.error || "Credenciales incorrectas"));
            }
          };
        };

        return (
          <div className="topbar">
            <Original />
            <button
              onClick={handleLoginClick}
              style={{
                backgroundColor: "#1e88e5",
                color: "white",
                border: "none",
                padding: "6px 12px",
                borderRadius: "5px",
                marginLeft: "10px",
                cursor: "pointer",
                fontSize: "14px",
              }}
            >
               Login
            </button>
          </div>
        );
      },
    },
  };
};

