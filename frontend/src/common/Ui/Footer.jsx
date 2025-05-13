export default function Footer() {
  return (
    <footer className="fixed-bottom bg-warning-subtle p-4 text-white text-center py-3">
      <div className="container">
        &copy; 2025 PetHotel &nbsp;
        <a href="/about" className="text-muted">About</a>
        <a href="/contact" className="text-body-secondary">Contact</a>
      </div>
    </footer>
  )
}