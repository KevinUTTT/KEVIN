import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SimulacionCajerosGUI extends JFrame {

    private JTextArea outputTextArea;
    private Queue<Integer> colaClientes;
    private int[] tiemposCajeros;
    private int[] clientesAtendidos;
    private int[] tiempoAcumulado;
    private int totalClientes;
    private Timer timer;

    public SimulacionCajerosGUI() {
        super("Simulación de Cajeros");

        outputTextArea = new JTextArea(20, 50);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        add(scrollPane);

        // Número de cajeros
        int numCajeros = 4;

        // Inicializar variables para la simulación
        colaClientes = new LinkedList<>();
        tiemposCajeros = new int[numCajeros];
        clientesAtendidos = new int[numCajeros];
        tiempoAcumulado = new int[numCajeros];
        totalClientes = 20;

        // Configurar temporizador para la simulación
        int delay = 1000; // 1000 milisegundos (1 segundo)
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simularPasoDeTiempo();
            }
        });

        // Botón para iniciar la simulación
        JButton startButton = new JButton("Iniciar Simulación");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSimulacion();
            }
        });
        add(startButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void iniciarSimulacion() {
        // Reiniciar variables para la simulación
        colaClientes.clear();
        for (int i = 0; i < tiemposCajeros.length; i++) {
            tiemposCajeros[i] = 0;
            clientesAtendidos[i] = 0;
            tiempoAcumulado[i] = 0;
        }

        // Iniciar temporizador
        timer.start();
    }

    private void simularPasoDeTiempo() {
        // Generar tiempo de atención aleatorio entre 1 y 10 segundos para el nuevo cliente
        int tiempoAtencion = new Random().nextInt(10) + 1;

        // Agregar el tiempo de atención del cliente a la cola
        colaClientes.offer(tiempoAtencion);

        // Actualizar la interfaz gráfica con la cola de clientes
        outputTextArea.append("Cola de clientes: " + colaClientes + "\n");

        // Asignar clientes a cajeros disponibles
        for (int cajero = 0; cajero < tiemposCajeros.length; cajero++) {
            if (tiemposCajeros[cajero] == 0 && !colaClientes.isEmpty()) {
                // Tomar un cliente de la cola y asignarlo al cajero
                int clienteActual = colaClientes.poll();
                tiemposCajeros[cajero] = clienteActual;
                clientesAtendidos[cajero]++;

                // Actualizar la interfaz gráfica con la información del cliente atendido
                outputTextArea.append("Cajero " + (cajero + 1) + " atendiendo cliente " + clienteActual + "\n");
            }

            // Actualizar tiempos de cajeros
            if (tiemposCajeros[cajero] > 0) {
                tiemposCajeros[cajero]--;
                tiempoAcumulado[cajero]++;
            }
        }

        // Actualizar la interfaz gráfica con un separador
        outputTextArea.append("-------------------------\n");

        // Detener la simulación si se han atendido todos los clientes
        if (clientesAtendidos[0] + clientesAtendidos[1] + clientesAtendidos[2] + clientesAtendidos[3] == totalClientes) {
            timer.stop();

            // Calcular tiempo promedio de atención
            int totalTiempoAcumulado = 0;
            int totalClientesAtendidos = 0;

            for (int cajero = 0; cajero < tiemposCajeros.length; cajero++) {
                totalTiempoAcumulado += tiempoAcumulado[cajero];
                totalClientesAtendidos += clientesAtendidos[cajero];

                outputTextArea.append("Cajero " + (cajero + 1) + ": Tiempo total = " + tiempoAcumulado[cajero] +
                        ", Clientes atendidos = " + clientesAtendidos[cajero] + "\n");
            }

            double tiempoPromedio = (double) totalTiempoAcumulado / totalClientesAtendidos;

            outputTextArea.append("Tiempo promedio de atención: " + tiempoPromedio + " segundos\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimulacionCajerosGUI();
            }
        });
    }
}
