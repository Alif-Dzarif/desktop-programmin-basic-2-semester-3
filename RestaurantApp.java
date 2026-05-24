import java.util.ArrayList;
import java.util.Scanner;

// ============================================================
//  Kelas Menu
// ============================================================
class Menu {
    private String nama;
    private double harga;
    private String kategori; // "makanan" atau "minuman"

    public Menu(String nama, double harga, String kategori) {
        this.nama   = nama;
        this.harga  = harga;
        this.kategori = kategori;
    }

    // Getter & Setter
    public String getNama()          { return nama; }
    public double getHarga()         { return harga; }
    public String getKategori()      { return kategori; }
    public void setHarga(double h)   { this.harga = h; }

    @Override
    public String toString() {
        return String.format("%-25s  Rp %,9.0f  [%s]", nama, harga, kategori);
    }
}

// ============================================================
//  Kelas Utama (Main)
// ============================================================
public class RestaurantApp {

    // ── Data ──────────────────────────────────────────────────
    static ArrayList<Menu> daftarMenu  = new ArrayList<>();
    static ArrayList<Menu> pesanan     = new ArrayList<>();   // item yang dipesan
    static ArrayList<Integer> jumlahPesanan = new ArrayList<>(); // jumlah tiap item
    static Scanner sc = new Scanner(System.in);

    // ── Konstanta biaya ───────────────────────────────────────
    static final double PAJAK_PERSEN     = 0.10;
    static final double BIAYA_PELAYANAN  = 20_000;
    static final double DISKON_THRESHOLD = 100_000;
    static final double DISKON_PERSEN    = 0.10;
    static final double BUYONEGET1_THRESHOLD = 50_000;

    // ============================================================
    //  main
    // ============================================================
    public static void main(String[] args) {
        inisialisasiMenu();

        boolean jalan = true;
        while (jalan) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║        RESTORAN NUSANTARA        ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Menu Pelanggan               ║");
            System.out.println("║  2. Menu Pengelolaan (Pemilik)   ║");
            System.out.println("║  0. Keluar                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Pilih: ");

            String pil = sc.nextLine().trim();
            switch (pil) {
                case "1" -> menuPelanggan();
                case "2" -> menuPengelolaan();
                case "0" -> { System.out.println("Terima kasih! Sampai jumpa."); jalan = false; }
                default  -> System.out.println("[!] Pilihan tidak valid, coba lagi.");
            }
        }
    }

    // ============================================================
    //  Inisialisasi menu awal (≥ 4 makanan + ≥ 4 minuman)
    // ============================================================
    static void inisialisasiMenu() {
        // Makanan
        daftarMenu.add(new Menu("Nasi Goreng Spesial",  25_000, "makanan"));
        daftarMenu.add(new Menu("Mie Ayam Bakso",       20_000, "makanan"));
        daftarMenu.add(new Menu("Ayam Bakar",           35_000, "makanan"));
        daftarMenu.add(new Menu("Soto Betawi",          28_000, "makanan"));
        daftarMenu.add(new Menu("Gado-Gado",            18_000, "makanan"));
        // Minuman
        daftarMenu.add(new Menu("Es Teh Manis",          5_000, "minuman"));
        daftarMenu.add(new Menu("Jus Alpukat",          15_000, "minuman"));
        daftarMenu.add(new Menu("Es Jeruk",              8_000, "minuman"));
        daftarMenu.add(new Menu("Kopi Susu",            12_000, "minuman"));
        daftarMenu.add(new Menu("Air Mineral",           5_000, "minuman"));
    }

    // ============================================================
    //  Tampilkan daftar menu (dikelompokkan per kategori)
    // ============================================================
    static void tampilkanDaftarMenu() {
        System.out.println("\n─────────── DAFTAR MENU ───────────");

        System.out.println("\n  ★ MAKANAN");
        int no = 1;
        for (int i = 0; i < daftarMenu.size(); i++) {
            if (daftarMenu.get(i).getKategori().equals("makanan")) {
                System.out.printf("  %2d. %s%n", no++, daftarMenu.get(i));
            }
        }

        System.out.println("\n  ★ MINUMAN");
        for (int i = 0; i < daftarMenu.size(); i++) {
            if (daftarMenu.get(i).getKategori().equals("minuman")) {
                System.out.printf("  %2d. %s%n", no++, daftarMenu.get(i));
            }
        }
        System.out.println("────────────────────────────────────");
    }

    // ============================================================
    //  Tampilkan menu dengan nomor global (1..N)
    // ============================================================
    static void tampilkanMenuBernomor() {
        System.out.println("\n─── MENU (pilih nomor) ───");
        // Makanan dulu
        int no = 1;
        System.out.println("  [MAKANAN]");
        for (Menu m : daftarMenu) {
            if (m.getKategori().equals("makanan"))
                System.out.printf("  %2d. %s%n", no++, m);
        }
        System.out.println("  [MINUMAN]");
        for (Menu m : daftarMenu) {
            if (m.getKategori().equals("minuman"))
                System.out.printf("  %2d. %s%n", no++, m);
        }
    }

    // Kembalikan Menu berdasar nomor tampil (urutan: makanan dulu, lalu minuman)
    static Menu getMenuByNomor(int nomor) {
        ArrayList<Menu> urut = new ArrayList<>();
        for (Menu m : daftarMenu) if (m.getKategori().equals("makanan")) urut.add(m);
        for (Menu m : daftarMenu) if (m.getKategori().equals("minuman")) urut.add(m);
        if (nomor < 1 || nomor > urut.size()) return null;
        return urut.get(nomor - 1);
    }

    // ============================================================
    //  MENU PELANGGAN
    // ============================================================
    static void menuPelanggan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n┌──────────────────────────────┐");
            System.out.println("│       MENU PELANGGAN         │");
            System.out.println("├──────────────────────────────┤");
            System.out.println("│  1. Lihat Daftar Menu        │");
            System.out.println("│  2. Pesan Makanan/Minuman    │");
            System.out.println("│  3. Lihat Pesanan Saat Ini   │");
            System.out.println("│  4. Selesai & Bayar          │");
            System.out.println("│  0. Kembali                  │");
            System.out.println("└──────────────────────────────┘");
            System.out.print("Pilih: ");

            String pil = sc.nextLine().trim();
            switch (pil) {
                case "1" -> tampilkanDaftarMenu();
                case "2" -> prosesPemesanan();
                case "3" -> lihatPesananSaatIni();
                case "4" -> { selesaiDanBayar(); kembali = true; }
                case "0" -> kembali = true;
                default  -> System.out.println("[!] Pilihan tidak valid.");
            }
        }
    }

    // ── Proses pemesanan ─────────────────────────────────────
    static void prosesPemesanan() {
        System.out.println("\n[Ketik 'selesai' untuk mengakhiri pemesanan]");
        while (true) {
            tampilkanMenuBernomor();
            System.out.print("\nNomor menu (atau 'selesai'): ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("selesai")) break;

            int nomor;
            try { nomor = Integer.parseInt(input); }
            catch (NumberFormatException e) {
                System.out.println("[!] Input tidak valid, masukkan nomor menu.");
                continue;
            }

            Menu pilihan = getMenuByNomor(nomor);
            if (pilihan == null) {
                System.out.println("[!] Nomor tidak ada dalam daftar menu.");
                continue;
            }

            System.out.print("Jumlah: ");
            String inputJml = sc.nextLine().trim();
            int jml;
            try { jml = Integer.parseInt(inputJml); }
            catch (NumberFormatException e) {
                System.out.println("[!] Jumlah tidak valid.");
                continue;
            }
            if (jml <= 0) { System.out.println("[!] Jumlah harus > 0."); continue; }

            // Cek apakah sudah ada di pesanan
            boolean ada = false;
            for (int i = 0; i < pesanan.size(); i++) {
                if (pesanan.get(i).getNama().equals(pilihan.getNama())) {
                    jumlahPesanan.set(i, jumlahPesanan.get(i) + jml);
                    ada = true; break;
                }
            }
            if (!ada) {
                pesanan.add(pilihan);
                jumlahPesanan.add(jml);
            }
            System.out.printf("[✓] %s x%d ditambahkan.%n", pilihan.getNama(), jml);
        }
    }

    // ── Lihat pesanan saat ini ───────────────────────────────
    static void lihatPesananSaatIni() {
        if (pesanan.isEmpty()) { System.out.println("\n[i] Belum ada pesanan."); return; }
        System.out.println("\n─── Pesanan Saat Ini ───");
        for (int i = 0; i < pesanan.size(); i++) {
            System.out.printf("  %s x%d = Rp %,.0f%n",
                pesanan.get(i).getNama(),
                jumlahPesanan.get(i),
                pesanan.get(i).getHarga() * jumlahPesanan.get(i));
        }
    }

    // ── Hitung total kotor (sebelum pajak/pelayanan) ─────────
    static double hitungTotalKotor() {
        double total = 0;
        for (int i = 0; i < pesanan.size(); i++)
            total += pesanan.get(i).getHarga() * jumlahPesanan.get(i);
        return total;
    }

    // ── Selesai dan bayar ────────────────────────────────────
    static void selesaiDanBayar() {
        if (pesanan.isEmpty()) { System.out.println("\n[i] Tidak ada pesanan."); return; }
        cetakStruk();
        pesanan.clear();
        jumlahPesanan.clear();
        System.out.println("\n[✓] Pesanan selesai. Terima kasih!");
    }

    // ============================================================
    //  Hitung Total Biaya & Cetak Struk
    // ============================================================
    static void cetakStruk() {
        double totalKotor = hitungTotalKotor();

        // Diskon
        double diskon = 0;
        boolean adaDiskon = false;
        if (totalKotor > DISKON_THRESHOLD) {
            diskon = totalKotor * DISKON_PERSEN;
            adaDiskon = true;
        }

        // Buy 1 get 1 minuman
        boolean adaBuyOneGet1 = false;
        String namaMinumanGratis = "";
        double hargaMinumanGratis = 0;
        if (totalKotor > BUYONEGET1_THRESHOLD) {
            // Cari minuman termurah yang dipesan
            double hargaTerendah = Double.MAX_VALUE;
            for (int i = 0; i < pesanan.size(); i++) {
                if (pesanan.get(i).getKategori().equals("minuman")) {
                    if (pesanan.get(i).getHarga() < hargaTerendah) {
                        hargaTerendah       = pesanan.get(i).getHarga();
                        namaMinumanGratis   = pesanan.get(i).getNama();
                        hargaMinumanGratis  = pesanan.get(i).getHarga();
                        adaBuyOneGet1       = true;
                    }
                }
            }
        }

        double totalSetelahDiskon = totalKotor - diskon - hargaMinumanGratis;
        double pajak   = totalSetelahDiskon * PAJAK_PERSEN;
        double totalAkhir = totalSetelahDiskon + pajak + BIAYA_PELAYANAN;

        // ── Cetak ──────────────────────────────────────────────
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           STRUK PEMBAYARAN             ║");
        System.out.println("║          RESTORAN NUSANTARA            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf ("║  %-38s║%n", "Item Pesanan:");
        System.out.println("╠════════════════════════════════════════╣");

        for (int i = 0; i < pesanan.size(); i++) {
            String baris = String.format("%s x%d", pesanan.get(i).getNama(), jumlahPesanan.get(i));
            double total  = pesanan.get(i).getHarga() * jumlahPesanan.get(i);
            System.out.printf("║  %-26s Rp%,8.0f ║%n", baris, total);
        }

        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf ("║  %-26s Rp%,8.0f ║%n", "Total Kotor",  totalKotor);

        if (adaDiskon) {
            System.out.printf("║  %-26s Rp%,8.0f ║%n", "Diskon 10%", -diskon);
        }
        if (adaBuyOneGet1) {
            String ket = "Gratis: " + namaMinumanGratis;
            System.out.printf("║  %-26s Rp%,8.0f ║%n", ket, -hargaMinumanGratis);
        }

        System.out.printf ("║  %-26s Rp%,8.0f ║%n", "Setelah Diskon", totalSetelahDiskon);
        System.out.printf ("║  %-26s Rp%,8.0f ║%n", "Pajak 10%", pajak);
        System.out.printf ("║  %-26s Rp%,8.0f ║%n", "Biaya Pelayanan", BIAYA_PELAYANAN);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf ("║  %-26s Rp%,8.0f ║%n", "TOTAL AKHIR", totalAkhir);
        System.out.println("╚════════════════════════════════════════╝");
    }

    // ============================================================
    //  MENU PENGELOLAAN (Pemilik)
    // ============================================================
    static void menuPengelolaan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n┌──────────────────────────────────┐");
            System.out.println("│     MENU PENGELOLAAN PEMILIK     │");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│  1. Lihat Daftar Menu            │");
            System.out.println("│  2. Tambah Menu Baru             │");
            System.out.println("│  3. Ubah Harga Menu              │");
            System.out.println("│  4. Hapus Menu                   │");
            System.out.println("│  0. Kembali                      │");
            System.out.println("└──────────────────────────────────┘");
            System.out.print("Pilih: ");

            String pil = sc.nextLine().trim();
            switch (pil) {
                case "1" -> tampilkanDaftarMenu();
                case "2" -> tambahMenu();
                case "3" -> ubahHargaMenu();
                case "4" -> hapusMenu();
                case "0" -> kembali = true;
                default  -> System.out.println("[!] Pilihan tidak valid.");
            }
        }
    }

    // ── Tambah menu baru ──────────────────────────────────────
    static void tambahMenu() {
        System.out.println("\n─── Tambah Menu Baru ───");

        String nama;
        while (true) {
            System.out.print("Nama menu baru: ");
            nama = sc.nextLine().trim();
            if (!nama.isEmpty()) break;
            System.out.println("[!] Nama tidak boleh kosong.");
        }

        double harga = 0;
        while (true) {
            System.out.print("Harga (Rp): ");
            try {
                harga = Double.parseDouble(sc.nextLine().trim());
                if (harga > 0) break;
                System.out.println("[!] Harga harus > 0.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Input tidak valid.");
            }
        }

        String kategori;
        while (true) {
            System.out.print("Kategori (makanan/minuman): ");
            kategori = sc.nextLine().trim().toLowerCase();
            if (kategori.equals("makanan") || kategori.equals("minuman")) break;
            System.out.println("[!] Kategori harus 'makanan' atau 'minuman'.");
        }

        // Konfirmasi
        System.out.printf("%nKonfirmasi tambah '%s' - Rp %.0f [%s]? (Ya/Tidak): ", nama, harga, kategori);
        String konfirm = sc.nextLine().trim();
        if (konfirm.equalsIgnoreCase("Ya")) {
            daftarMenu.add(new Menu(nama, harga, kategori));
            System.out.println("[✓] Menu berhasil ditambahkan.");
        } else {
            System.out.println("[i] Pembatalan. Menu tidak ditambahkan.");
        }
    }

    // ── Ubah harga menu ───────────────────────────────────────
    static void ubahHargaMenu() {
        if (daftarMenu.isEmpty()) { System.out.println("[i] Daftar menu kosong."); return; }
        tampilkanMenuBernomor();

        int nomor = pilihNomorMenu("Nomor menu yang ingin diubah harganya");
        if (nomor == -1) return;
        Menu m = getMenuByNomor(nomor);

        double hargaBaru;
        while (true) {
            System.out.print("Harga baru (Rp): ");
            try {
                hargaBaru = Double.parseDouble(sc.nextLine().trim());
                if (hargaBaru > 0) break;
                System.out.println("[!] Harga harus > 0.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Input tidak valid.");
            }
        }

        System.out.printf("Konfirmasi ubah harga '%s' dari Rp %.0f → Rp %.0f? (Ya/Tidak): ",
            m.getNama(), m.getHarga(), hargaBaru);
        String konfirm = sc.nextLine().trim();
        if (konfirm.equalsIgnoreCase("Ya")) {
            m.setHarga(hargaBaru);
            System.out.println("[✓] Harga berhasil diubah.");
        } else {
            System.out.println("[i] Perubahan dibatalkan.");
        }
    }

    // ── Hapus menu ────────────────────────────────────────────
    static void hapusMenu() {
        if (daftarMenu.isEmpty()) { System.out.println("[i] Daftar menu kosong."); return; }
        tampilkanMenuBernomor();

        int nomor = pilihNomorMenu("Nomor menu yang ingin dihapus");
        if (nomor == -1) return;
        Menu m = getMenuByNomor(nomor);

        System.out.printf("Konfirmasi hapus '%s'? (Ya/Tidak): ", m.getNama());
        String konfirm = sc.nextLine().trim();
        if (konfirm.equalsIgnoreCase("Ya")) {
            daftarMenu.remove(m);
            System.out.println("[✓] Menu berhasil dihapus.");
        } else {
            System.out.println("[i] Penghapusan dibatalkan.");
        }
    }

    // ── Helper: minta input nomor menu yang valid ─────────────
    static int pilihNomorMenu(String pesan) {
        while (true) {
            System.out.print(pesan + ": ");
            String input = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(input);
                if (getMenuByNomor(n) != null) return n;
                System.out.println("[!] Nomor tidak ada dalam daftar menu.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Input tidak valid, masukkan nomor menu.");
            }
        }
    }
}